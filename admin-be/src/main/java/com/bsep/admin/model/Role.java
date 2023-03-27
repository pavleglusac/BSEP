package com.bsep.admin.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
	ROLE_USER,
	ROLE_ADMIN;

	public SimpleGrantedAuthority toAuthority() {
		return new SimpleGrantedAuthority(this.name());
	}
}