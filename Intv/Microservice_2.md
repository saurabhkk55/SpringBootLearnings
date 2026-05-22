Leys say, two services update the same data at the same time and causing data conflicts. So How will you handle concurrent updates in microservices?

There are several ways to handle concurrent updates:
1. `Optimistic Locking`: We add a version number to data. When updating, check if version is still same. If changed then reject and retry.
   - Example: let's say service A reads version 1. Service B updates and now version becomes 2. Service A tries to update but rejected due to version mismatch

2. `Pessimistic Locking`: Here, we lock the data while updating so other services must wait. It is good for critical data but slower than Optimistic locking

3. `Idempotency`: Here we ensure same request doesn't update data twice. Let me tell how it works, each request has a unique ID (known as idempotency key), system stores it and If same ID comes again then ignore or return old response

---

Suppose a service publishes events, but due to a bug, it starts sending incorrect data to multiple services. So how will you prevent this?

There are several ways to prevent this:
- Every service should double-check incoming data like rules, schema and required fields.
- We must define a fixed event format like a contract and If the event doesn't match then ignore it.
- We should use Idempotency technique to make updates safe so even if bad data comes twice, it doesn't keep corrupting things.
- We should use circuit breaker, If one service starts sending bad events then stop consuming from it temporarily.
- We should monitor, the logs, graphs, and alerts regularly

---

You have a centralized logging system, but logs from different services are hard to debug. So how will you trace a request across services?

- In order to fix it, we need a way to tie all logs of one request together.
- When a request starts we should generate a unique ID (like abc-123). Then we have to pass this ID to every service (via headers) and Every service logs this same ID. Now we could search logs using that ID And see the full journey of that request.
- For example, user made a request to service A to service B and then service C, there would be a single traceId for the all services logs

---

Your system uses message queues, but messages are getting stuck and not processed. So How will you debug and fix message backlog issues?

It's like a Traffic Jam; vehicles are getting stuck and not able to get the clear road so we have to find where the jam is happening and fix it.
- First, we have to check whether there is a bad message that is blocking everything, if it is there, then we have to remove or skip that bad message because sometimes one message keeps failing again and again
- Second, we have to check whether the services are running successfully because they may be crashed or stopped so that case, we have to restart those.
- Third, we have to check number of worker or service instances, if they are one they increase those
- Fourth, we have to check whether the workers or services too slow, if yes then we have to check the slow APIS, and improve the performance
- Fifth, we have to slow down the incoming task if these are coming too fast, for example If 100 task/sec coming but only processed 10 task/sec

---

Let's say a service is accidentally exposed to outside and starts receiving malicious traffic. How will you secure your internal services?

Inorder to secure our internal services, we could follow several steps

- Don't expose internal services directly: Internal services should NOT be public, Put them behind an API Gateway and Only the gateway should be exposed, not our services
- Restrict who can talk to whom: Even inside system, don't trust everything. Allow only specific services to call each other and block unknown requests
- Add authentication between services: Each service should prove its identity by tokens like JWT

If too many requests come then we should use rate limiting to limit the requests per second.

---

Let's say You need to release a feature to only 10% of users initially. So How will you implement feature rollout?

- We would take a stable identity like the user ID, generate a consistent number using hashing, and assign each user to one of 100 buckets using modulo (% 100).
- Then we would enable the feature only for buckets 0-9 to achieve roughly 10% rollout. This ensures the same users always see the feature without it changing randomly.

---

Suppose that your microservices system has too many inter-service calls, and causing high latency. So do we have any solution to reduce this latency?

Yes, we can reduce the latency by these solutions:

- Instead of calling 5 services separately, create one layer that fetches all data in one go.
- Don't call service again and again for same data and instead we should use caching.
- Analysis and avoid unnecessary and repetitive calls.
- We could also use asyn communication and now we don't have to wait for every response

---

Let's say, you are migrating a monolith to microservices, but some modules are tightly coupled. So how will you break them into services safely?

- We could add the timeouts and now we don't have to wait for the slow service
- We could use circuit breaker if service is consistently slow or failing.
- We could add the fallback response, meaning if service fails return the default data
- We could also use caching for the repetitive data calls.
- We could increase the instances of the services in order to decrease the latency

---

Lets say Your system has multiple services deployed globally, but users face latency issues based on location. So How will you design for low latency globally?

When users are in different parts of the world, delay happens because data has to travel far. 
- To fix this, We would place our services in multiple locations (regions) so users connect to the nearest one. 
- We would use routing to send each user to the closest server automatically. 
- We would also store frequently used data in cache or CDN so it loads faster from nearby. 
- For databases, We would keep copies in different regions so data can be read locally. 
- This way, users don't have to wait for data to travel long distances, and everything feels faster.

---

Assume that one service works on critical financial data, and failure is not acceptable. So How will you ensure high reliability and fault tolerance to your client or project manager?

- First, we would run multiple instances of the service (no single point of failure), so if one goes down, others continue working.
- We would store data safely using replication (multiple copies of data) and regular backups to prevent loss.
- We would also use retries and failover mechanisms so if one component fails, another takes over automatically. To avoid wrong data, I would use transactions and validation checks.

---

Lets say you need to process millions of requests per second. So How will you design your microservices for extreme scalability?

- First, We would keep services stateless, so we can quickly add more instances when traffic increases.
- Then we would use horizontal scaling (adding more servers instead of bigger ones) behind a load balancer to distribute traffic evenly.
- We would use caching to reduce repeated work and database load.
- For heavy or non-urgent tasks, We would use asynchronous processing (queues) so requests don't block.
- We would also use database scaling techniques like sharding and read replicas to handle large data loads.
- And finally, we would monitor traffic and auto-scale services based on demand.

---

Lets say you have deployed a service successfully, but suddenly you observed that there is CPU usage spikes. How will you debug this production performance issue?

- First I would check the recent code changes, if they are buggy or there are infinite loops then definitely we will rollback and fix it.
- If the code is not buggy then We would check the traffic, if requests suddenly increases or system is overloaded then we would scale more instances.
- We have to Identify heavy APIs as well, there may be a chance, one api is being called too much.
- We will also check DB queries and if they are heavy then we have to optimize those.

---

Lets say your system needs to support real-time updates like chat or notifications. So How will you design this in microservices?

- To support real-time updates like chat or notifications, I would keep a live connection between the client and server using something like WebSockets, so the server can push updates instantly instead of the client repeatedly asking for them.
- When an event happens (like a new message), it is sent to a message queue, and a dedicated notification or chat service picks it up and delivers it to the user in real time.
- I would also store messages in a database so users can see them later, and handle offline users by saving messages and sending them when they come back online. This way, the system stays fast, scalable, and reliable

---

Assume that You are using third-party APIs, and they start failing randomly. So how would you debug and resolve this issue?

- When third-party APIs start failing randomly, I would first confirm and understand the failures by checking logs, error codes, and patterns (like timeouts or specific endpoints failing). Then I would verify if the issue is from their side by checking their status page or trying the API manually.
- To protect my system, I would add timeouts so we don't wait too long, and use retries with limits for temporary failures. I would also implement a circuit breaker so if the API keeps failing, we stop calling it for some time and avoid overloading our system. For better user experience, I would use also fallbacks like cached or default responses.

---

Lets say you notice that a single user request triggers 50+ internal service calls. So is this a good design? And If not, so how will you fix it?

- No, this is not a good design because one request triggering 50+ calls means higher latency, more chances of failure, and unnecessary load on the system.
- To fix this, I would first try to reduce the number of calls by combining multiple calls into a single request using an API composition layer or gateway. I would also check if some data can be cached so we don't call services repeatedly for the same information. If certain data is needed frequently, I might store a copy locally to avoid calling another service every time.

---

If network latency increases, will microservices still be better than monolith?

- Not always, if network latency increases, microservices can actually become worse than a monolith in some cases.
- In microservices, services talk to each other over the network, so higher latency means every call takes longer, and multiple calls can slow the system a lot. In a monolith, everything runs in the same process, so communication is much faster (no network delay). So if your system has many inter-service calls and high latency, a monolith can perform better.

---

How would you test microservices, please share 2-3 strategies?

To test microservices, I would use a few simple but effective strategies:
- First, unit testing, where I test each service's logic independently by mocking external dependencies. This ensures the service works correctly on its own.
- Second, integration testing, where I test how services interact with each other or with databases and APIs. This helps catch issues in communication between services.
- Third, end-to-end testing, where I test the complete flow (like a real user request) across multiple services to ensure the whole system works together properly.

---

How would you scale databases in microservices without any downtime?

- To scale databases in microservices without downtime, I would avoid making big changes all at once and instead scale gradually while the system is still running. 
- I would add read replicas to handle more read traffic and reduce load on the main database, and for heavy scale I would use sharding to split data across multiple databases. 
- To avoid downtime, I would run the new setup alongside the old one and shift traffic slowly (rolling or blue-green approach). 
- This way, users are never affected while scaling happens in the background.

---

How would you handle schema changes in microservice architecture?

- For handling schema changes, I would follow a backward-compatible approach so old and new versions of services can work together. 
- For example, instead of modifying or deleting a column directly, I would first add new fields, update the services to use them, and only later remove old ones. 
- I would deploy changes in steps and ensure both schemas work during the transition.

