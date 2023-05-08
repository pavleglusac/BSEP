package com.bsep.admin.auth;

import com.bsep.admin.auth.AuthService;
import com.bsep.admin.auth.dto.LoggedUserDto;
import com.bsep.admin.auth.dto.LoginRequest;
import com.bsep.admin.auth.dto.RegistrationRequest;
import com.bsep.admin.auth.dto.TokenResponse;
import com.bsep.admin.exception.ForbiddenRealEstateAction;
import com.bsep.admin.model.User;
import com.bsep.admin.myHouse.dto.RealEstateDto;
import com.bsep.admin.users.dto.UserDisplayDto;
import jakarta.servlet.Registration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	// login

	@PostMapping("/login")
	public TokenResponse login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		return authService.login(loginRequest, response);
	}

	@GetMapping("/privileged")
	@PreAuthorize("hasAuthority('CERTIFICATE_MANAGEMENT')")
	public String privileged() {
		return "Privileged";
	}

	@PostMapping("/register")
	@PreAuthorize("hasAuthority('WRITE_USER')")
	public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegistrationRequest loginRequest, HttpServletResponse response) {
		authService.register(loginRequest, response);
		return ResponseEntity.ok(Map.of("message", "User successfully registered. Please check your email for verification."));
	}

	@GetMapping("/verify")
	public ResponseEntity<String> verify(@RequestParam("token") String token, @RequestParam("email") String email) {
		authService.verify(email, token);
		return ResponseEntity.ok("Verified");
	}

	@GetMapping("/my-profile")
	@PreAuthorize("hasAuthority('READ_PROFILE')")
	public ResponseEntity<LoggedUserDto> findRealEstatesForUser(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(new LoggedUserDto(user.getId(), user.getName(), user.getEmail(), user.getImageUrl(), user.getRoles().get(0).getName()));
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
		User user = (User) authentication.getPrincipal();
		authService.logout(user, request, response);
		return ResponseEntity.ok("Logged out");
	}

}
