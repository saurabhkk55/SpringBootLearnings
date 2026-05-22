You have 3 microservices: Order Service, Payment Service and Inventory Service. Order Service calls Payment and then payment calls Inventory. So, What happens if Payment succeeds but Inventory service fails?

- If Payment succeeds but Inventory fails, we get an inconsistent state.
- Money is deducted, but the item is not reserved. So, the order cannot be completed.
- To fix this, we should use:
    - Rollback/Refund mechanism where Payment is reversed
    - Retry mechanism where we Try Inventory again
    - Saga pattern where Automatically undoes previous steps

---

How Saga Pattern automatically undoes, could you please explain its mechanism thoroughly?

- Saga Pattern works like a step-by-step transaction with a backup plan.
- A big process is broken into smaller steps (Example, Order → Payment → Inventory).
- Each step completes its own work and saves it.
- If all steps succeed process is done
- If any step fails system runs compensating actions (undo steps).
- Example: If Payment succeeds but Inventory fails, then Saga triggers a refund to undo the payment.

---

Could you please explain Choreography and Orchestration styles of saga pattern?

`Choreography`:
- In choreography, there is no central controller. Each service works independently and reacts to events. For example, when Order Service creates an order, it sends an event. Payment Service listens to it, processes payment, and then sends another event. Inventory Service listens and continues the process. So basically, services talk through events and decide themselves what to do next.

`Orchestration`:
- In orchestration, there is a central coordinator (orchestrator) that controls everything. It tells each service what to do step by step. For example, the orchestrator tells Payment Service to process payment, then tells Inventory Service to update stock. If something fails, it also tells services how to undo actions. So here, one central service manages the whole flow.

---

In microservices, how would you handle transactions across multiple services?

- In microservices, we don't use one big database transaction across services (like in monolithics), because each service has its own database.
- Instead, we use the Saga Pattern. As we already covered this, the transaction is broken into small steps, and each service completes its part. If all steps succeed transaction is complete.
- If something fails in between, we don't "rollback" like a database. Instead, we run compensating actions (or undo steps). For example, if payment is done but inventory fails, we trigger a refund.

---

Each microservice has its own database, but sometimes you need data from another service. So will you call another service every time or duplicate the data?

- In microservices, we should not call another service every time, because it can slow things down and create dependency between services.
- Instead, a common approach is to duplicate only the required data (called data replication). Services share updates through events, so each service keeps a small copy of the data it needs. This makes systems faster and more reliable.

---

Why does each microservice should have its own database?

- Each microservice should have its own database to stay independent. This means one service can change, scale, or even fail without affecting others.
- If multiple services share the same database, they become tightly connected. A small change in one service can break others, and making the system hard to manage

---

Your system has an API Gateway, and suddenly traffic increases heavily. So what would you do in this situation?

- First, we would scale the API Gateway horizontally (add more instances) and put a load balancer in front to distribute traffic.
- Second, we would apply rate limiting so one user can't overload the system, and use caching to reduce repeated requests.
- Third, we would use a queue (like Kafka/RabbitMQ) for heavy operations so requests don't fail immediately.

---

Why should we use an API Gateway instead of calling services directly?

- We use an API Gateway to simplify and secure communication between clients and microservices.
- If clients call services directly, they need to know multiple service URLs, handle different responses, and manage complexity. With an API Gateway, the client calls one single entry point, and the gateway routes requests to the right service.
- It also handles common tasks like authentication, logging, rate limiting, and caching, so each service doesn't have to do it.

---

In Microservice architecture, Share the scenarios when would you choose synchronous and asynchronous

- We should use synchronous communication when we need an instant response.
- For example, login, payment confirmation, validate user, etc.
- We should use asynchronous communication when work can happen in the background. For example, sending emails, notifications, updating reports, analytics, Long-running tasks and Reducing load during high traffic

---

One of your services is down, but your application keeps calling it repeatedly. How will you handle this?

If one service is down and our app keeps calling it again and again, it can overload the system and make things worse.

To handle this, we use a few simple techniques:
- recover. Circuit Breaker: Stop calling the failed service for some time after repeated failures. Let it
- Retry with delay: Instead of calling immediately again, wait (like a few seconds) before retrying.
- Fallback: Show a default response (like "service unavailable, try later") instead of failing completely.
- Timeout: Don't wait forever for a response, wait only for a fixed time (say 2 seconds), then stop and move on.

---

A request fails somewhere across 5 microservices. How will you debug?

First, we should start by tracking the request end-to-end. Then Use a request ID so we can follow the same request across all 5 microservices in logs. Check logs step-by-step to see where it first failed.

Then, check each service one by one:
- Look at logs, errors, and response codes
- Verify if any service is slow, down, or returning wrong data
- Check dependencies like database, API calls, or queues
- Finally, use tools like distributed tracing (e.g., Zipkin, Jaeger) and monitoring dashboards to quickly spot the failing service. Once found, fix the issue and add alerts to catch it earlier next time.

---

You want to deploy a new version without downtime. How will you do it?

Use Zero Downtime Deployment techniques:

- Blue-Green Deployment: Run old (blue) and new (green) versions side by side. Switch traffic to the new version once it's ready.
- Rolling Deployment: Update servers one by one so the app stays live.
- Canary Deployment: Release to a small group first, then gradually to all 

This way, users don't face any downtime during the update.

---

A service fails due to temporary issue. So how will you handle this situation?

Use Retry with delay.
- If service fails, wait for a few seconds and try again (retry).
- Limit retries (e.g., 3 times) to avoid overload.
- We could Use Circuit Breaker: if it keeps failing, stop calling for some time.
- We could Add fallback (like show "try again later" or use cached data).

---

Let say Payment API is called twice accidentally. How will you ensure duplicate payment doesn't happen?

- We should Use Idempotency here, basically.
- Send a unique transaction ID with each payment request.
- If same request comes again, system checks ID and ignores duplicate.
- Store request status to prevent double processing.

---

One service is getting heavy load. So how will you scale it?

- We can scale horizontally by adding more instances of the service and use a load balancer to distribute traffic.
- We can also enable auto-scaling so instances increase or decrease based on traffic.
- If needed, we can also optimize code or add caching to reduce load.

---

You have 10 microservices with environment configs. How will you manage configs?

Here we will use a centralized config management system where all configs are stored in one place. Each service will fetch configs from there. This helps in easy updates, consistency, and avoids hardcoding configs in services

---

Your services work fine in lower env but fails in production. How would you debug it?

- First, We will check logs to find errors, compare configs between environments, and verify environment variables. 
- We will also check data differences and external dependencies. 
- If needed, We could also try to reproduce the issue in staging env.

---

Let's say Service A calls Service B, but B is down. What happens?

- In this case, Service A's request will fail or get delayed. 
- To handle this, we can use retries with limits, timeouts, and circuit breaker pattern to stop repeated calls. 
- Also, we can return a fallback response so the system doesn't completely fail.

---

One microservice is slow and impacting the system. What will you do?

- First, we will find the cause using logs and monitoring.
- Then we can optimize code, improve database queries, or add caching.
- We could scale the service (add more instances).
- Also, we could also use timeout or async calls to reduce impact on other services.

---

How would you handle security in microservices?

- First, we will secure APIs using authentication and authorization (like JWT or OAuth) and use HTTPS for all communication.
- Then we will validate inputs, avoid exposing sensitive data, and use API Gateway for centralized security.
- Each service should have proper access control and secrets should be stored securely.

---

When should we NOT use microservice architecture?

- We should not use microservices for small or simple applications.
- If the team is small or not experienced, it adds unnecessary complexity.
- Also, if strong coordination and fast development are needed, monolith is better.
- Microservices are useful only when the system is large and needs scalability.
