### Preview: https://www.youtube.com/watch?v=2sdY-fKaR2o (Spring Batch Processing : A Comprehensive Step-by-Step Tutorial)

### Question: 
A file has 10K rows of data, we can effectively store it into db using spring batch. But, what if we have 700-900 files and each file has 10k of rows. Can we solve it using ExecutorService / CompletableFuture and SpringBatch?

---

I’ll explain **Spring Batch Partitioning with `ThreadPoolTaskExecutor`** step-by-step with **complete theory + working code** in a simple way.

Your case:

```
900 files
Each file = 10k rows
Total rows ≈ 9M
```

Goal:

```
Process files in parallel
Insert data efficiently into DB
Avoid memory issues
```

Spring Batch provides **Partitioning** exactly for this problem.

---

# 1️⃣ What is Partitioning in Spring Batch?

Partitioning means:

> Split one big job into **multiple smaller jobs** and run them **in parallel**.

Example:

```
900 files
```

Instead of

```
Job
 └─ Step
      └─ process files one by one
```

We do

```
Job
 └─ Master Step
        │
        ├─ Worker Step 1 → File1
        ├─ Worker Step 2 → File2
        ├─ Worker Step 3 → File3
        ├─ Worker Step 4 → File4
        ...
```

Each worker step runs **in parallel thread**.

Thread pool controls concurrency.

---

# 2️⃣ Complete Architecture

```
Job
 │
 └── Master Step
        │
        ├── FilePartitioner
        │        │
        │        ├ file1.csv
        │        ├ file2.csv
        │        ├ file3.csv
        │        └ file900.csv
        │
        └── Worker Step (Parallel)
                 │
                 ├ Reader
                 ├ Processor
                 └ Writer
```

---

# 3️⃣ Project Dependencies

Example Maven dependency:

```xml
<dependencies>

<dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-batch</artifactId>
</dependency>

<dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
 <groupId>mysql</groupId>
 <artifactId>mysql-connector-j</artifactId>
</dependency>

</dependencies>
```

---

# 4️⃣ Example File

Example CSV

```
id,name,age
1,Rahul,25
2,Amit,30
3,Sara,29
```

---

# 5️⃣ Entity

```java
@Entity
public class Person {

    @Id
    private Long id;

    private String name;

    private Integer age;

}
```

---

# 6️⃣ Item Reader

This reader will read **one file**.

```java
@Bean
@StepScope
public FlatFileItemReader<Person> reader(
        @Value("#{stepExecutionContext['fileName']}") String fileName) {

    FlatFileItemReader<Person> reader = new FlatFileItemReader<>();

    reader.setResource(new FileSystemResource(fileName));
    reader.setLinesToSkip(1);

    DefaultLineMapper<Person> mapper = new DefaultLineMapper<>();

    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
    tokenizer.setNames("id","name","age");

    BeanWrapperFieldSetMapper<Person> fieldSetMapper =
            new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(Person.class);

    mapper.setLineTokenizer(tokenizer);
    mapper.setFieldSetMapper(fieldSetMapper);

    reader.setLineMapper(mapper);

    return reader;
}
```

Important:

```
@StepScope
```

This allows **each partition to receive different file name**.

---

# 7️⃣ Processor

Optional business logic.

```java
@Bean
public ItemProcessor<Person, Person> processor() {

    return person -> {

        // Example logic
        person.setName(person.getName().toUpperCase());

        return person;
    };
}
```

---

# 8️⃣ Writer

Writes batch to database.

```java
@Bean
public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {

    JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<>();

    writer.setDataSource(dataSource);

    writer.setSql("""
        INSERT INTO person(id,name,age)
        VALUES(:id,:name,:age)
    """);

    writer.setItemSqlParameterSourceProvider(
            new BeanPropertyItemSqlParameterSourceProvider<>());

    return writer;
}
```

This performs **batch insert**.

---

# 9️⃣ Worker Step

Worker step processes **one file**.

```java
@Bean
public Step workerStep(StepBuilderFactory stepBuilderFactory,
                       FlatFileItemReader<Person> reader,
                       ItemProcessor<Person, Person> processor,
                       JdbcBatchItemWriter<Person> writer) {

    return stepBuilderFactory.get("workerStep")
            .<Person, Person>chunk(500)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
}
```

Chunk explanation:

```
read 500 rows
process them
write them in single DB batch
```

---

# 🔟 File Partitioner

Partitioner splits files.

```java
public class FilePartitioner implements Partitioner {

    private String directory;

    public FilePartitioner(String directory) {
        this.directory = directory;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<String, ExecutionContext> partitions = new HashMap<>();

        File folder = new File(directory);

        File[] files = folder.listFiles();

        int partitionNumber = 0;

        for (File file : files) {

            ExecutionContext context = new ExecutionContext();

            context.putString("fileName", file.getAbsolutePath());

            partitions.put("partition" + partitionNumber, context);

            partitionNumber++;
        }

        return partitions;
    }
}
```

This produces:

```
partition0 → file1.csv
partition1 → file2.csv
partition2 → file3.csv
```

---

# 1️⃣1️⃣ Thread Pool

Controls **parallel threads**.

```java
@Bean
public TaskExecutor taskExecutor() {

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(20);
    executor.setQueueCapacity(50);

    executor.initialize();

    return executor;
}
```

Meaning:

```
10 threads always active
max 20 threads
```

So:

```
20 files processed simultaneously
```

---

# 1️⃣2️⃣ Master Step

Master step controls partitioning.

```java
@Bean
public Step masterStep(StepBuilderFactory stepBuilderFactory,
                       Step workerStep,
                       TaskExecutor taskExecutor) {

    return stepBuilderFactory.get("masterStep")

            .partitioner("workerStep",
                    new FilePartitioner("/data/files"))

            .step(workerStep)

            .gridSize(20)

            .taskExecutor(taskExecutor)

            .build();
}
```

Explanation:

```
gridSize = 20
```

Means

```
20 partitions run in parallel
```

---

# 1️⃣3️⃣ Job

```java
@Bean
public Job job(JobBuilderFactory jobBuilderFactory,
               Step masterStep) {

    return jobBuilderFactory.get("fileProcessingJob")
            .start(masterStep)
            .build();
}
```

---

# 1️⃣4️⃣ Execution Flow

If you have

```
900 files
```

Flow becomes:

```
Job start
   ↓
Master Step
   ↓
FilePartitioner
   ↓
Create 900 partitions
   ↓
ThreadPoolTaskExecutor
   ↓
Run 20 partitions in parallel
   ↓
Each partition executes Worker Step
   ↓
Reader → Processor → Writer
```

---

# 1️⃣5️⃣ Real Runtime Example

Example:

```
Thread1 → file1.csv
Thread2 → file2.csv
Thread3 → file3.csv
Thread4 → file4.csv
...
Thread20 → file20.csv
```

When thread1 finishes:

```
Thread1 → file21.csv
```

---

# 1️⃣6️⃣ Performance Improvement

Sequential:

```
900 files
1 file = 5 seconds

Total = 4500 sec
= 75 minutes
```

Parallel:

```
20 threads

4500 / 20
= 225 seconds
≈ 4 minutes
```

Huge improvement 🚀

---

# 1️⃣7️⃣ Production Best Practices

### 1. Use chunking

```
chunk(500)
```

Avoid:

```
chunk(1)
```

---

### 2. Limit threads

Good values:

```
10–30 threads
```

Too many threads = DB overload.

---

### 3. DB batch insert

Enable batching:

```
spring.jpa.properties.hibernate.jdbc.batch_size=500
```

---

### 4. Streaming reader

Never load full file.

Spring Batch already streams lines.

---

# 1️⃣8️⃣ Final Architecture (Production)

```
Input Files (900)
        │
        ▼
Spring Batch Job
        │
        ▼
Master Step
        │
        ▼
File Partitioner
        │
        ▼
ThreadPoolTaskExecutor (20 threads)
        │
        ▼
Worker Step
        │
        ▼
Reader → Processor → Writer
        │
        ▼
DB Batch Insert
```

---

✅ This architecture is used in **banks, telecom, and ETL systems**.

---

If you want, I can also show you **a much more advanced architecture** used for **1000+ files and 100M rows** using:

```
Spring Batch + Virtual Threads (Java 21)
+ Async ItemProcessor
+ Async ItemWriter
```

That architecture is **5–10x faster than normal Spring Batch**.
