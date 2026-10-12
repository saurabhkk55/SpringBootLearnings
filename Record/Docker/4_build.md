# Method 1: Create an image using a Dockerfile, then create a container

### Step 1: Create a Dockerfile

```dockerfile
FROM openjdk:21

WORKDIR /app

COPY app.jar .

CMD ["java", "-jar", "app.jar"]
```

### Step 2: Build the image

```bash
docker build -t my-java-app .
```

### Step 3: Create and start the container

```bash
docker run -d --name java-container my-java-app
```

---

# Method 2: Pull an existing image from Docker Hub, then create a container

### Pull image

```bash
docker pull nginx
```

### Create and start container

```bash
docker run -d --name my-nginx -p 80:80 nginx
```

Or simply:

```bash
docker run -d --name my-nginx -p 80:80 nginx
```

If the image doesn't exist locally, Docker automatically downloads it.

---

# Method 3: Create an image from an existing container, then create another container

Suppose you already have a running container.

### Check running containers

```bash
docker ps
```

Example

```
CONTAINER ID   IMAGE    NAME
a12bc34de56    ubuntu   my-container
```

### Save the container as a new image

```bash
docker commit my-container my-custom-image
```

### Verify

```bash
docker images
```

You'll see

```
REPOSITORY        TAG
my-custom-image   latest
```

### Create another container from that image

```bash
docker run -it --name another-container my-custom-image
```

---

So, if an interviewer asks, **"What are the ways to create a Docker container?"**, the most accurate answer is:

1. **Build an image from a Dockerfile** (`docker build`), then create a container with `docker run`.
2. **Pull an image from a registry such as Docker Hub** (`docker pull` or implicitly via `docker run`), then create a container with `docker run`.
3. **Create a new image from an existing container** (`docker commit`), then create another container from that image using `docker run`.
