package com.ktds.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails{

	private String email;
	private String password;
	private String grade;
	private boolean isBlockUser;
	
	
	
	public User(String email, String password, String grade, boolean isBlockUser) {
		this.email = email;
		this.password = password;
		this.grade = grade;
		this.isBlockUser = isBlockUser;
	}

	public void setBlockUser(boolean isBlockUser) {
		this.isBlockUser = isBlockUser;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add( new SimpleGrantedAuthority( grade ) );
		if ( grade.equals("ROLE_ADMIN") ) {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
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
		return this.isBlockUser;
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
