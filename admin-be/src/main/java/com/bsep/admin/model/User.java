package com.bsep.admin.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@SQLDelete(sql = "UPDATE USERS SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class User implements UserDetails {

	@Id
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(unique = true, nullable = false)
	private String email;

	private String imageUrl;

	private Integer loginAttempts = 0;

	private String password;

	private Boolean deleted = Boolean.FALSE;

	@Column(name = "LOGIN_TOKEN")
	private String loginToken;

	@Column(name = "EMAIL_VERIFIED")
	private Boolean emailVerified = false;

	@Column(name = "EMAIL_VERIFICATION_TOKEN")
	private String emailVerificationToken;

	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany()
	private List<RealEstate> realEstates;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(this.role.toAuthority());
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return loginAttempts < 3;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return emailVerified;
	}

}