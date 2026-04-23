package ro.tacklestore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import ro.tacklestore.repository.AdminUserRepository;
import ro.tacklestore.security.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final AdminUserRepository adminUserRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            var admin = adminUserRepository.findByUsername(username).orElseThrow();
            String token = jwtUtil.generateToken(admin.getUsername(), admin.getRole());

            return ResponseEntity.ok(Map.of(
                "token", token,
                "username", admin.getUsername(),
                "role", admin.getRole()
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
}

