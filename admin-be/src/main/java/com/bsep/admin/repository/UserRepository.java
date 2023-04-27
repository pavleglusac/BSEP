package com.bsep.admin.repository;

import com.bsep.admin.model.Role;
import com.bsep.admin.model.User;
import com.bsep.admin.users.dto.UserDisplayDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String email);

	Optional<User> findByEmailVerificationToken(String token);
	@Query("SELECT u FROM User u WHERE " +
			"(u.name LIKE %:query% OR u.email LIKE %:query%) AND u.deleted = false " +
			"AND u.role IN :roles AND (u.loginAttempts >= 3 OR (u.loginAttempts < 3 AND :onlyLocked = false))")
	Page<User> search(
			@Param("query") String query,
			@Param("roles") EnumSet<Role> roles,
			@Param("onlyLocked") boolean onlyLocked,
			PageRequest pageRequest);
}
