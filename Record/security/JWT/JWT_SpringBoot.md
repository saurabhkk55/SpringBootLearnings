# 🔐 Spring Boot JWT Authentication – Complete Practical Guide

Is document mein hum **step‑by‑step ek complete Spring Boot application** design karenge jisme **JWT (JSON Web Token)** use hoga.

Main **simple words + real-life flow + complete code + Postman examples** ke saath explain kar raha hoon — bilkul interview + real project ready 👌

---

## 📌 What we will build

* User **Signup**
* User **Login**
* JWT **Access Token**
* JWT **Refresh Token** (expiry ke baad naya token)
* **JWT Validation**
* **JWT Filter** (Spring Security)
* **Role-based Authorization**

---

# 1️⃣ Dependencies (`pom.xml`)

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>

    <!-- H2 (for demo) -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

---

# 2️⃣ User Entity (DB)

```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private String role; // ROLE_USER, ROLE_ADMIN
}
```

---

# 3️⃣ JWT Utility Class

### 🔹 JWT create + validate + extract

```java
@Component
public class JwtUtil {

    private final String SECRET = "my-secret-key-123456";
    private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 5; // 5 min
    private final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24; // 1 day

    public String generateAccessToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

---

# 4️⃣ JWT Filter (MOST IMPORTANT)

### 🤔 JWT Filter kya karta hai?

Socho JWT ek **ID card** hai jo user har request ke saath dikhaata hai.

👉 **JWT Filter ka kaam**:

1. Har incoming request ko intercept karna
2. Header se JWT token nikalna
3. Token valid hai ya nahi check karna
4. Agar valid hai → user ko **Spring Security context** mein set karna

> 🔥 Agar JWT Filter nahi hoga → Spring Security ko pata hi nahi chalega user kaun hai

---

### 🔹 Flow samjho (real life)

```
Client -----> API Request + JWT
                |
                v
           JWT Filter
                |
        Token valid? ❌ → 401
                |
               ✅
                |
     User authenticated in context
                |
           Controller call
```

---

### 🔹 Code: JwtFilter

```java
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1️⃣ Authorization header read karo
        String authHeader = request.getHeader("Authorization");

        // 2️⃣ Check: header hai + Bearer se start ho raha?
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // 3️⃣ Actual JWT token nikalo
            String token = authHeader.substring(7);

            // 4️⃣ Token valid hai?
            if (jwtUtil.isTokenValid(token)) {

                // 5️⃣ Username extract karo
                String username = jwtUtil.extractUsername(token);

                // 6️⃣ DB / UserDetails se user load karo
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 7️⃣ Authentication object banao
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 8️⃣ Spring Security ko bata do: user authenticated hai
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 9️⃣ Request ko aage jaane do
        filterChain.doFilter(request, response);
    }
}
```

---

# 5️⃣ Spring Security Configuration

### 🤔 SecurityConfig kyun chahiye?

Spring Security by default:

* Har API ko **secure** kar deta hai
* Login page expect karta hai (form based)

👉 Hume chahiye:

* JWT based security
* Kuch APIs public (`/auth/**`)
* Baaki sab secure

---

### 🔹 Flow samjho

```
Request ---> Security Filter Chain
              |
              |--> JWT Filter
              |--> Authorization rules
```

---

### 🔹 Code: SecurityConfig

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // CSRF disable (JWT stateless hai)
            .csrf().disable()

            // Authorization rules
            .authorizeHttpRequests()
            .requestMatchers("/auth/**").permitAll()  // login, signup open
            .anyRequest().authenticated()              // baaki sab secure

            // JWT filter add karo
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

# 6️⃣ Auth Controller (Signup + Login + Refresh)

```java
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        User dbUser = userRepository.findByUsername(user.getUsername()).orElseThrow();

        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtUtil.generateAccessToken(dbUser.getUsername(), dbUser.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(dbUser.getUsername());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestParam String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username).orElseThrow();

        String newAccessToken = jwtUtil.generateAccessToken(username, user.getRole());

        return Map.of("accessToken", newAccessToken);
    }
}
```

---

# 7️⃣ Access Token vs Refresh Token (MOST CONFUSING PART)

### 🔑 Access Token

* Short lived (5–15 min)
* Har API request ke saath jaata hai
* Agar leak ho jaaye → damage limited

👉 Use when:

```
GET /api/data
Authorization: Bearer <access-token>
```

---

### 🔄 Refresh Token

* Long lived (1–7 days)
* Sirf **naya access token** lene ke liye
* Har request mein mat bhejo ❌

👉 Use when:

```
Access token expire ho gaya
```

---

### 🔁 Flow diagram

```
Login
  ↓
Access Token (5 min) + Refresh Token (1 day)
  ↓
API calls (Access Token)
  ↓
Access Token expired ❌
  ↓
/auth/refresh (Refresh Token)
  ↓
New Access Token ✅
```

---

# 8️⃣ How JWT works – SIGNUP (New User)

1. Client → `/auth/signup`
2. User saved in DB
3. User → `/auth/login`
4. Token generate

> Signup ke time token dena optional hota hai (best practice: login ke baad token)

---

# 9️⃣ Create a Secured API (JWT required)

### 🔹 Controller method

```java
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello! You accessed this API using JWT";
    }
}
```

---

# 🔟 Postman Endpoints + Response

### 🔹 Signup

`POST /auth/signup`

```json
{
  "username": "saurabh",
  "password": "1234"
}
```

---

### 🔹 Login

`POST /auth/login`

```json
{
  "username": "saurabh",
  "password": "1234"
}
```

Response:

```json
{
  "accessToken": "eyJhbGci...",
  "refreshToken": "eyJhbGci..."
}
```

---

### 🔹 Access Secured API

`GET /api/hello`

Headers:

```
Authorization: Bearer <accessToken>
```

Response:

```
Hello! You accessed this API using JWT
```

---

# 🔟 Role Based Authorization using `@PreAuthorize`

Ab hum **real power of JWT** use karenge 👉 **ROLE based access** 🔥

---

## 🤔 `@PreAuthorize("hasRole('ADMIN')")` ka matlab kya hai?

Simple words mein:

👉 **Sirf wahi user is method ko access kar sakta hai jiske paas `ROLE_ADMIN` ho**

Agar user ke JWT token mein:

```
role = ROLE_USER
```

❌ Access denied

Agar:

```
role = ROLE_ADMIN
```

✅ Access allowed

---

## 1️⃣ JWT token mein ROLE kaise jaata hai?

Login ke time hum already role daal rahe hain:

```java
public String generateAccessToken(String username, String role) {
    return Jwts.builder()
            .setSubject(username)
            .claim("role", role) // 👈 role stored inside JWT
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
            .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
            .compact();
}
```

👉 Matlab **JWT khud bol raha hai**: user ka role kya hai

---

## 2️⃣ JwtFilter role ko Spring Security tak kaise pahunchata hai?

### 🔹 Step-by-step magic 🪄

JWT Filter:

1. Token read karta hai
2. Username nikalta hai
3. `UserDetailsService` se user load karta hai
4. User ke roles → `GrantedAuthority` ban jaate hain

```java
UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities() // 👈 ROLE_ADMIN / ROLE_USER
        );

SecurityContextHolder.getContext().setAuthentication(authentication);
```

👉 Ab Spring Security ko pata hai:

```
User = saurabh
Authorities = [ROLE_ADMIN]
```

---

## 3️⃣ Method level security enable karna (IMPORTANT)

### ❌ Iske bina `@PreAuthorize` kaam hi nahi karega

```java
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig {
}
```

---

## 4️⃣ Admin-only API using `@PreAuthorize`

```java
@RestController
@RequestMapping("/admin")
public class AdminController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Welcome ADMIN! You have full access.";
    }
}
```

---

## 5️⃣ Complete Flow (JWT + @PreAuthorize)

```
Client Request
   ↓
Authorization: Bearer <JWT>
   ↓
JwtFilter validates token
   ↓
User + ROLE loaded into SecurityContext
   ↓
@PreAuthorize checks role
   ↓
ROLE_ADMIN ? ✅ : ❌ 403
```

---

## 6️⃣ Postman Test Scenarios

### 🔹 Case-1: USER role token ❌

JWT payload:

```json
{
  "sub": "saurabh",
  "role": "ROLE_USER"
}
```

Request:

```
GET /admin/dashboard
Authorization: Bearer <USER_TOKEN>
```

Response:

```
403 Forbidden
```

---

### 🔹 Case-2: ADMIN role token ✅

JWT payload:

```json
{
  "sub": "admin",
  "role": "ROLE_ADMIN"
}
```

Response:

```
Welcome ADMIN! You have full access.
```

---

## 🔥 Interview GOLD Lines

* `@PreAuthorize` works on **Spring Security Context**
* JWT filter populates **Authentication object**
* Roles come from `GrantedAuthority`
* JWT itself does NOT authorize → Spring Security does

---

# 🔟 Final Summary

* JWT stores role as claim
* JwtFilter converts role → authority
* `@PreAuthorize` checks authority
* Access control happens at **method level**

---


---

Bhai 🔥 **ab hum JWT ka “real power zone” touch kar rahe hain**
`@PreAuthorize("hasRole('ADMIN')")` sirf annotation nahi hai — poora **Spring Security + JWT pipeline** ka final checkpoint hai.

Good news 👉 **maine document ko update kar diya hai** aur ab ye crystal clear ho gaya hai ✅
Main yahan short + sharp + mental-model way mein samjha deta hoon.

---

Perfect bhai 👍
Ab **pure Postman-ready endpoints** de raha hoon — **copy-paste + test** mode 💯
Main **role-wise + JWT-wise** clearly dikha raha hoon.

---

# 🧪 POSTMAN ENDPOINTS – COMPLETE LIST (JWT + ROLES)

Base URL maan lo:

```
http://localhost:8080
```

---

## 1️⃣ SIGNUP (New User)

### 🔹 Endpoint

```
POST /auth/signup
```

### 🔹 Body (JSON)

```json
{
  "username": "saurabh",
  "password": "1234"
}
```

### 🔹 Response

```
User registered successfully
```

📌 Default role: `ROLE_USER`

---

## 2️⃣ LOGIN (Existing User)

### 🔹 Endpoint

```
POST /auth/login
```

### 🔹 Body

```json
{
  "username": "saurabh",
  "password": "1234"
}
```

### 🔹 Response

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

📌 **Access Token → har API ke liye**
📌 **Refresh Token → naya access token**

---

## 3️⃣ PUBLIC API (No JWT needed)

(agar tumne koi open API banayi ho)

```
GET /auth/health
```

Response:

```
OK
```

---

## 4️⃣ SECURED API (JWT REQUIRED)

### 🔹 Endpoint

```
GET /api/hello
```

### 🔹 Headers

```
Authorization: Bearer <ACCESS_TOKEN>
```

### 🔹 Response

```
Hello! You accessed this API using JWT
```

❌ Token nahi diya → `401 Unauthorized`

---

## 5️⃣ ADMIN ONLY API (`@PreAuthorize`)

### 🔹 Endpoint

```
GET /admin/dashboard
```

### 🔹 Headers

```
Authorization: Bearer <ACCESS_TOKEN>
```

---

### 🔴 Case-1: USER token (ROLE_USER)

JWT payload:

```json
{
  "sub": "saurabh",
  "role": "ROLE_USER"
}
```

Response:

```
403 Forbidden
```

---

### 🟢 Case-2: ADMIN token (ROLE_ADMIN)

JWT payload:

```json
{
  "sub": "admin",
  "role": "ROLE_ADMIN"
}
```

Response:

```
Welcome ADMIN! You have full access.
```

---

## 6️⃣ REFRESH TOKEN (Access token expire hone par)

### 🔹 Endpoint

```
POST /auth/refresh
```

### 🔹 Params (or Body)

```
refreshToken=<REFRESH_TOKEN>
```

### 🔹 Response

```json
{
  "accessToken": "new-access-token-here"
}
```

📌 **Is token ko phir se APIs mein use karo**

---

## 🔁 REAL TEST FLOW (Postman order)

```
1️⃣ Signup
2️⃣ Login → access + refresh token
3️⃣ /api/hello → access token
4️⃣ /admin/dashboard → admin only
5️⃣ Access token expire
6️⃣ /auth/refresh
7️⃣ New access token → APIs
```

---

## 🔥 Interview Tip (Postman ke saath bolne wali line)

> “We authenticate using JWT, authorize using Spring Security method-level annotations like @PreAuthorize, and refresh access tokens using a refresh token endpoint.”
