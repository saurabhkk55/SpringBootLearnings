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

# 3. Check Reverse Proxy / Ingress Limits

Very common production issue.

Even if Spring Boot allows 100MB,
request may never reach application because:

* Nginx
* Kubernetes Ingress
* API Gateway
* Load Balancer

may reject request first.

---

# Upload Request Flow

```text id="jlwm26"
Client
  ↓
Nginx / Ingress / Gateway
  ↓
Load Balancer
  ↓
Spring Boot Application
```

If proxy layer blocks request:

```text id="jlwm27"
Application never receives request
```

So:

* controller not called
* exception handler not triggered
* logs may show nothing

This is why issue appears as:

```text id="jlwm28"
Silent failure
```

---

# Nginx Default Upload Limit

Default Nginx upload limit is commonly:

```text id="jlwm29"
client_max_body_size = 1MB
```

Very common root cause.

---

# Configure Nginx Properly

```nginx id="jlwm30"
client_max_body_size 100M;
proxy_read_timeout 300;
proxy_connect_timeout 300;
proxy_send_timeout 300;
```

---

# Kubernetes Nginx Ingress Default

Usually behaves similarly to Nginx defaults.

Common default:

```text id="jlwm31"
1MB
```

unless overridden.

---

# Configure Kubernetes Ingress Properly

```yaml id="jlwm32"
nginx.ingress.kubernetes.io/proxy-body-size: "100m"
nginx.ingress.kubernetes.io/proxy-read-timeout: "300"
nginx.ingress.kubernetes.io/proxy-send-timeout: "300"
```

Without this:

* Spring Boot config becomes useless
* request blocked before app receives it

---

# API Gateway Limits

Different gateways have different limits.

Examples:

| Service              | Common Limit   |
| -------------------- | -------------- |
| AWS API Gateway REST | 10MB           |
| Nginx                | 1MB default    |
| Kubernetes Ingress   | Often 1MB      |
| Cloudflare           | Plan dependent |

Always verify infrastructure-level limits.

---

# 4. Configure Timeout Properly

Large uploads take time.

If timeout is too small:

* upload terminates midway
* connection closes
* client sees timeout/reset
* upload appears to fail silently

---

# Spring Boot / Tomcat Timeout

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

# Example Production Issue

```text id="jlwm35"
Tomcat timeout = 5m
Nginx timeout = 60s
```

Result:

```text id="jlwm36"
Upload fails after 60 seconds
```

because Nginx terminates connection first.

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

# Best Enterprise Approach — Pre-Signed URL Upload

For very large uploads:

Avoid routing upload through application server.

---

# Flow

```text id="jlwm41"
Client requests upload URL
        ↓
Spring Boot generates pre-signed URL
        ↓
Client uploads directly to S3
```

Benefits:

* lower application memory usage
* better scalability
* reduced CPU/network overhead
* faster uploads

---

# 7. Validate File Size Early

Fail fast before processing.

Example:

```java id="jlwm42"
if (file.getSize() > MAX_SIZE) {
    throw new IllegalArgumentException("File too large");
}
```

---

# 8. Add Proper Logging

Enable logs to identify:

* multipart rejection
* timeout issues
* broken connections
* memory problems

Example:

```properties id="jlwm43"
logging.level.org.springframework.web=DEBUG
```

---

# Most Common Real Production Root Causes

Usually one of these:

```text id="jlwm44"
1. Spring multipart limit not configured
2. Nginx/Ingress limit smaller
3. Timeout at proxy/load balancer
4. Entire file loaded into memory
5. Exception not handled properly
```

---

# Recommended Enterprise Solution

## Small Files

Use normal multipart upload.

---

## Large Files

Use:

* streaming
* S3/object storage
* pre-signed URLs
* async processing if required

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
