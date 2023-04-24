package com.bsep.admin.auth;

import com.bsep.admin.auth.dto.LoginRequest;
import com.bsep.admin.auth.dto.RegistrationRequest;
import com.bsep.admin.auth.dto.TokenResponse;
import com.bsep.admin.exception.UserNotFoundException;
import com.bsep.admin.model.Role;
import com.bsep.admin.model.User;
import com.bsep.admin.repository.UserRepository;
import com.bsep.admin.security.CustomAuthenticationToken;
import com.bsep.admin.security.TokenProvider;
import com.bsep.admin.service.MailingService;
import com.bsep.admin.util.Trie;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.UUID;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Trie trie;

	@Autowired
	private MailingService mailingService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	public TokenResponse login(LoginRequest loginRequest, HttpServletResponse response) {
		String secret = generateRandomToken(20);

		Authentication authentication = authenticationManager.authenticate(new CustomAuthenticationToken(
				loginRequest.getEmail(),
				loginRequest.getPassword(),
				loginRequest.getLoginToken(),
				secret
		));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String accessToken = tokenProvider.createAccessToken(authentication, secret);
		Long expiresAt = tokenProvider.readClaims(accessToken).getExpiration().getTime();
		// add cookie with secret
		Cookie cookie = new Cookie("secret", secret);
		cookie.setPath("/");
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(60 * 60 * 24 * 30);
		response.addCookie(cookie);
		((User)authentication.getPrincipal()).setLoginAttempts(0);
		userRepository.save((User)authentication.getPrincipal());
		return new TokenResponse(accessToken, expiresAt);
	}

	public String generateRandomToken(int length) {
		String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!#$%&'()*+-./:<=>?@[]^_`{|}~";
		SecureRandom random = new SecureRandom();
		StringBuilder buffer = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(allowedChars.length());
			buffer.append(allowedChars.charAt(randomIndex));
		}
		String generatedString = buffer.toString();
		System.out.println(generatedString);
		return generatedString;
	}

	public Boolean checkPasswordStrength(String password) {
		if (password.length() < 12) {
			return false;
		}
		if (trie.find(password)) {
			return false;
		}
		boolean hasUppercase = !password.equals(password.toLowerCase());
		boolean hasLowercase = !password.equals(password.toUpperCase());
		boolean hasNumber = password.matches(".*\\d.*");
		boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+].*");
		return hasUppercase && hasLowercase && hasNumber && hasSpecialChar;
	}

	public void register(RegistrationRequest registrationRequest, HttpServletResponse response) {
		// check if password is strong enough
		if (!checkPasswordStrength(registrationRequest.getPassword())) {
			throw new RuntimeException("Password is not strong enough");
		}
		// check if email is already taken
		if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
			throw new RuntimeException("Email is already taken");
		}
		// create user
		createUser(registrationRequest);
	}

	private void createUser(RegistrationRequest registrationRequest) {
		User user = new User();

		if (Objects.equals(registrationRequest.getRole(), "ROLE_TENNANT")) {
			user.setRole(Role.ROLE_TENNANT);
		} else if (Objects.equals(registrationRequest.getRole(), "ROLE_LANDLORD")) {
			user.setRole(Role.ROLE_LANDLORD);
		} else {
			throw new RuntimeException("Invalid role");
		}
		user.setId(UUID.randomUUID());
		user.setEmailVerified(false);
		user.setEmail(registrationRequest.getEmail());
		String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
		user.setPassword(encodedPassword);
		user.setName(registrationRequest.getName());
		user.setLoginAttempts(0);

		String loginToken = generateRandomToken(20);
		user.setLoginToken(passwordEncoder.encode(loginToken));
		String registrationToken = generateRandomToken(20);
		user.setEmailVerificationToken(passwordEncoder.encode(registrationToken));

		userRepository.save(user);
		sendVerificationEmail(user, registrationToken, loginToken);
	}

	private void sendVerificationEmail(User user, String emailVerificationToken, String loginToken) {
		mailingService.sendVerificationMail(user.getName(), user.getEmail(), emailVerificationToken, loginToken);
	}

	public void verify(String email, String token) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
		if (passwordEncoder.matches(token, user.getEmailVerificationToken())) {
			user.setEmailVerified(true);
			userRepository.save(user);
		} else {
			throw new RuntimeException("Invalid token");
		}
	}
}
