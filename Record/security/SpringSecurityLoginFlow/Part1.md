# 🔐 Spring Security Login Flow — PART 1

## 👨‍💻 Developer Side Code (100% Complete & Detailed)

> **Focus of Part 1:**
>
> * Sirf **developer kya likhta hai**
> * **Full, runnable code** (no shortcuts)
> * Ye code likhne ke baad Spring Security ka engine kaam karna start karta hai

---

## 🧠 Mental Model (Before Any Code)

> Spring Security bolta hai:
> **"Mujhe user ka data do, main authentication & authorization sambhaal lunga"**

Developer ko bas ye batana hota hai:

1. User ka data kaisa hai
2. User kahan se load hoga
3. Password kaise check hoga

---

## 🗂️ Project Structure (Developer Side)

```
com.example.securitydemo
│
├── entity
│   └── UserEntity.java
│
├── repository
│   └── UserRepository.java
│
├── security
│   ├── CustomUserDetails.java
│   └── CustomUserDetailsService.java
│
├── config
│   └── SecurityConfig.java
│
└── SecurityDemoApplication.java
```

---

## 1️⃣ UserEntity (Database Model)

```java
package com.example.securitydemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // BCrypt encrypted

    @Column(nullable = false)
    private String role; // ADMIN / USER

    // getters & setters
}
```

📌 **Developer responsibility**

* User ka structure define karna
* Password encrypted store karna

---

## 2️⃣ UserRepository

```java
package com.example.securitydemo.repository;

import com.example.securitydemo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
}
```

📌 **Developer responsibility**

* User ko DB se nikalna

---

## 3️⃣ CustomUserDetails (IMPORTANT CLASS)

```java
package com.example.securitydemo.security;

import com.example.securitydemo.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(UserEntity user) {
        this.username = user.getUsername();
        this.password = user.getPassword();

        // ROLE_ prefix is mandatory
        this.authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

📌 **Developer responsibility**

* DB User → Spring Security User
* Roles mapping

---

## 4️⃣ CustomUserDetailsService

```java
package com.example.securitydemo.security;

import com.example.securitydemo.entity.UserEntity;
import com.example.securitydemo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
```

📌 **Developer responsibility**

* User lookup
* Exception throw karna

---

## 5️⃣ SecurityConfig (Minimum Working)

```java
package com.example.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

📌 **Developer responsibility**

* Security rules
* PasswordEncoder

---

## 🧠 END OF PART 1 SUMMARY

✔ Developer ne sirf ye bataya:

* User kya hai
* User kahan se milega
* Password kaise verify hoga

❌ Authentication object
❌ Filters
❌ SecurityContext

👉 **Ye sab Spring Security khud karega** (Part 2)

---

➡️ **Next: PART 2**

> Spring Security Internal Flow (unauthenticated → authenticated)
