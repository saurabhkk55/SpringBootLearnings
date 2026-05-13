# File Upload API Failing Silently for Large Files — Complete Notes

If a file upload API works for small files but fails for large files without proper error response, the issue is commonly caused by:

* multipart upload size limits
* reverse proxy limits
* timeout issues
* loading entire file into memory
* missing exception handling

---

# 1. Configure Multipart File Size Properly

Spring Boot has default multipart upload limits.

Configure them in:

```properties id="6k5v74"
application.properties
```

Example:

```properties id="k5d17t"
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
```

---

# Difference Between Both

## max-file-size

Maximum size of a single uploaded file.

Example:

```text id="1kvb0o"
One file = 80MB
Limit = 100MB
→ Allowed
```

---

## max-request-size

Maximum total multipart request size.

Example:

```text id="n7kbbh"
3 files × 40MB = 120MB
Limit = 100MB
→ Request rejected
```

Many developers configure only:

```properties id="jlwm21"
max-file-size
```

but forget:

```properties id="jlwm22"
max-request-size
```

Then upload still fails.

---

# Spring Boot Default Limits

Default values are commonly:

```text id="jlwm23"
max-file-size = 1MB
max-request-size = 10MB
```

So uploads beyond that fail unless explicitly configured.

---

# 2. Add Proper Exception Handling

Without exception handling:

* request may fail internally
* client receives generic 500
* connection reset may occur
* upload appears to fail silently

Add global exception handling.

Example:

```java id="jlwm24"
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(
            MaxUploadSizeExceededException ex) {

        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("Uploaded file exceeds allowed size");
    }
}
```

Now client receives:

```text id="jlwm25"
413 Payload Too Large
```

instead of silent failure.

---

# 3. Spring Boot / Tomcat Timeout

Example:

```properties id="jlwm33"
server.tomcat.connection-timeout=5m
```

Meaning:

```text id="jlwm34"
Tomcat waits 5 minutes for request activity
```

---

# Is 5 Minutes Enough?

Depends on:

* file size
* internet speed
* mobile network quality
* server load
* upload mechanism

---

# Usually Enough For

* 10MB–200MB uploads
* normal enterprise systems
* stable network connections

---

# May NOT Be Enough For

* GB-size uploads
* video uploads
* slow mobile internet
* cross-region uploads

---

# Important Reality About Timeout

Tomcat timeout alone is NOT enough.

Other layers may timeout earlier:

| Layer         | Possible Timeout   |
| ------------- | ------------------ |
| Browser       | Request timeout    |
| API Gateway   | 30s / 60s          |
| Nginx         | proxy timeout      |
| Load Balancer | Idle timeout       |
| Ingress       | proxy timeout      |
| Tomcat        | connection timeout |

Smallest timeout wins.

---

# 5. Avoid Loading Entire File Into Memory

Bad approach:

```java id="jlwm37"
byte[] bytes = file.getBytes();
```

Problem:

* entire file loaded into heap memory
* high memory consumption
* GC pressure
* OutOfMemoryError risk
* poor scalability

Especially dangerous for:

* videos
* ZIP files
* PDFs
* large CSVs

---

# Better Approach — Streaming

Use InputStream instead.

Example:

```java id="jlwm38"
InputStream inputStream = file.getInputStream();
```

Benefits:

* low memory usage
* scalable processing
* suitable for large files

---

# 6. Store Large Files in Object Storage

Avoid storing large files:

* inside DB
* inside pod/container filesystem
* fully in memory

Preferred:

* AWS S3
* Azure Blob Storage
* Google Cloud Storage

---

# Better Architecture

Instead of:

```text id="jlwm39"
Client → Spring Boot → DB
```

Use:

```text id="jlwm40"
Client → Spring Boot → S3
```

---

# Interview-Level Summary

> Large file upload failures are often caused by multipart size restrictions, reverse proxy limits, timeout issues, or memory-heavy processing.
>
> Important fixes:
>
> * Configure `spring.servlet.multipart.max-file-size`
> * Configure `spring.servlet.multipart.max-request-size`
> * Handle `MaxUploadSizeExceededException`
> * Configure Nginx/Ingress upload limits
> * Configure timeout across all layers
> * Use streaming instead of loading entire file into memory
> * Store files in object storage like S3
> * Use pre-signed URLs for very large uploads
>
> In production systems, infrastructure-level limits like Nginx or API Gateway commonly block uploads before the request even reaches Spring Boot.
