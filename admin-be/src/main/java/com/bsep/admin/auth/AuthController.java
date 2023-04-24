package com.bsep.admin.auth;

import com.bsep.admin.auth.AuthService;
import com.bsep.admin.auth.dto.LoginRequest;
import com.bsep.admin.auth.dto.RegistrationRequest;
import com.bsep.admin.auth.dto.TokenResponse;
import com.bsep.admin.model.User;
import jakarta.servlet.Registration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	// login

	@PostMapping("/login")
	public TokenResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		return authService.login(loginRequest, response);
	}

	@GetMapping("/privileged")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String privileged() {
		return "Privileged";
	}

	@PostMapping("/register")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> register(@RequestBody RegistrationRequest loginRequest, HttpServletResponse response) {
		authService.register(loginRequest, response);
		return ResponseEntity.ok("Registered");
	}

	@GetMapping("/verify")
	public ResponseEntity<String> verify(@RequestParam("token") String token, @RequestParam("email") String email) {
		authService.verify(email, token);
		return ResponseEntity.ok("Verified");
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) authentication.getPrincipal();
		authService.logout(user, request, response);
		return ResponseEntity.ok("Logged out");
	}

}
