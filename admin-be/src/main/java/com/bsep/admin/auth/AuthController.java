package com.bsep.admin.auth;

import com.bsep.admin.auth.AuthService;
import com.bsep.admin.auth.dto.LoginRequest;
import com.bsep.admin.auth.dto.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

}
