I’ll explain a **production-style architecture** where:

* **Source** → Azure Blob Storage
* **Processing** → Spring Batch Partitioning
* **Parallelism** → `ThreadPoolTaskExecutor`
* **Destination** → MongoDB
* **Files** → 700–900 files
* **Rows per file** → ~10k

Goal:

```
Azure Blob
   ↓
List files
   ↓
Spring Batch Partitioner
   ↓
Parallel Worker Steps
   ↓
Read file → Process → Save to MongoDB
```

I will also explain **secure Azure connection** (very important in real systems).

---

# 1. Overall Architecture

```
Azure Blob Container
      │
      │ list blobs
      ▼
Spring Batch Job
      │
      ▼
Master Step
      │
      ▼
Partitioner (each blob = partition)
      │
      ▼
ThreadPoolTaskExecutor
      │
      ▼
Worker Step (parallel)
      │
      ├── Download blob
      ├── Read rows
      ├── Process
      └── Save to MongoDB
```

Example runtime:

```
900 files
20 threads

Thread1  → file1.csv
Thread2  → file2.csv
Thread3  → file3.csv
...
Thread20 → file20.csv
```

---

# 2. Dependencies

```xml
<dependencies>

<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-batch</artifactId>
</dependency>

<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>

<dependency>
<groupId>com.azure</groupId>
<artifactId>azure-storage-blob</artifactId>
<version>12.25.0</version>
</dependency>

</dependencies>
```

---

# 3. Secure Azure Authentication

There are **3 secure methods**.

| Method            | Security | Recommended |
| ----------------- | -------- | ----------- |
| Connection String | Medium   | Small apps  |
| SAS Token         | High     | Good        |
| Managed Identity  | Highest  | Production  |

For now we use **Managed Identity / DefaultAzureCredential**.

---

# 4. Azure Configuration

```
application.yml
```

```yaml
azure:
  storage:
    account-name: mystorageaccount
    container-name: mycontainer
```

---

# 5. Azure Blob Client Configuration

```java
@Configuration
public class AzureBlobConfig {

    @Value("${azure.storage.account-name}")
    private String accountName;

    @Bean
    public BlobContainerClient blobContainerClient() {

        String endpoint = "https://" + accountName + ".blob.core.windows.net";

        BlobServiceClient blobServiceClient =
                new BlobServiceClientBuilder()
                        .endpoint(endpoint)
                        .credential(new DefaultAzureCredentialBuilder().build())
                        .buildClient();

        return blobServiceClient.getBlobContainerClient("mycontainer");
    }
}
```

### Why this is secure

`DefaultAzureCredential` automatically checks:

```
Environment variables
Azure CLI login
Managed Identity (AKS / VM)
```

No secrets stored in code.

---

# 6. MongoDB Configuration

```
application.yml
```

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/mydb
```

---

# 7. Mongo Entity

```java
@Document(collection = "person")
public class Person {

    @Id
    private String id;

    private String name;

    private int age;

}
```

---

# 8. Mongo Repository

```java
public interface PersonRepository extends MongoRepository<Person,String> {
}
```

---

# 9. Partitioner (List Azure Files)

Partitioner fetches all blob names.

```java
@Component
public class AzureBlobPartitioner implements Partitioner {

    private final BlobContainerClient containerClient;

    public AzureBlobPartitioner(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<String, ExecutionContext> partitions = new HashMap<>();

        int i = 0;

        for (BlobItem blobItem : containerClient.listBlobs()) {

            ExecutionContext context = new ExecutionContext();
            context.putString("blobName", blobItem.getName());

            partitions.put("partition" + i, context);

            i++;
        }

        return partitions;
    }
}
```

Example output:

```
partition0 → file1.csv
partition1 → file2.csv
partition2 → file3.csv
```

---

# 10. Azure Blob Reader

Each worker downloads file.

```java
@Component
@StepScope
public class AzureBlobItemReader implements ItemReader<String> {

    private final BlobContainerClient containerClient;

    @Value("#{stepExecutionContext['blobName']}")
    private String blobName;

    private BufferedReader reader;

    public AzureBlobItemReader(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    @PostConstruct
    public void init() {

        BlobClient blobClient = containerClient.getBlobClient(blobName);

        InputStream inputStream = blobClient.openInputStream();

        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public String read() throws Exception {

        return reader.readLine();
    }
}
```

This **streams file directly from Azure**.

No temp storage required.

---

# 11. Processor

Convert CSV → Entity.

```java
@Component
public class PersonProcessor implements ItemProcessor<String, Person> {

    @Override
    public Person process(String line) {

        String[] data = line.split(",");

        Person person = new Person();

        person.setId(data[0]);
        person.setName(data[1]);
        person.setAge(Integer.parseInt(data[2]));

        return person;
    }
}
```

---

# 12. Mongo Writer

```java
@Component
public class MongoPersonWriter implements ItemWriter<Person> {

    private final PersonRepository repository;

    public MongoPersonWriter(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(List<? extends Person> items) {

        repository.saveAll(items);
    }
}
```

Batch insert improves performance.

---

# 13. Worker Step

Processes **one file**.

```java
@Bean
public Step workerStep(StepBuilderFactory stepBuilderFactory,
                       AzureBlobItemReader reader,
                       PersonProcessor processor,
                       MongoPersonWriter writer) {

    return stepBuilderFactory.get("workerStep")
            .<String, Person>chunk(500)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
}
```

---

# 14. Thread Pool

Controls concurrency.

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
20 files processed simultaneously
```

---

# 15. Master Step

```java
@Bean
public Step masterStep(StepBuilderFactory stepBuilderFactory,
                       AzureBlobPartitioner partitioner,
                       Step workerStep,
                       TaskExecutor taskExecutor) {

    return stepBuilderFactory.get("masterStep")
            .partitioner("workerStep", partitioner)
            .step(workerStep)
            .gridSize(20)
            .taskExecutor(taskExecutor)
            .build();
}
```

---

# 16. Job

```java
@Bean
public Job job(JobBuilderFactory jobBuilderFactory,
               Step masterStep) {

    return jobBuilderFactory.get("azureBlobJob")
            .start(masterStep)
            .build();
}
```

---

# 17. Execution Flow

```
Start Job
   │
   ▼
Partitioner lists Azure blobs
   │
   ▼
Create partitions
   │
   ▼
ThreadPoolTaskExecutor
   │
   ▼
Parallel worker steps
   │
   ▼
Download blob
   │
   ▼
Read → Process → MongoDB
```

---

# 18. Performance Example

```
900 files
10k rows each
= 9M rows
```

Sequential processing:

```
1 file = 4 sec
900 files = 3600 sec
= 60 minutes
```

Parallel (20 threads):

```
3600 / 20
= 180 sec
≈ 3 minutes
```

Huge improvement.

---

# 19. Production Security Best Practice

Never store secrets in code.

Use:

```
Azure Key Vault
Managed Identity
DefaultAzureCredential
```

Then Spring Boot reads credentials automatically.

---

# 20. Production Architecture

```
Azure Blob Storage
       │
       ▼
Spring Batch Job
       │
       ▼
Partitioner
       │
       ▼
ThreadPoolTaskExecutor
       │
       ▼
Worker Step
       │
       ├ Download Blob
       ├ Parse rows
       ├ Process data
       └ Save to MongoDB
```

---

✅ This architecture is used in **large ETL systems processing millions of records daily**.

---

If you want, I can also show you a **much more powerful version used in large systems**:

```
Azure Blob
↓
Event Grid
↓
Kafka
↓
Spring Batch consumers
↓
MongoDB
```

That architecture can process **100M+ records/day**.
