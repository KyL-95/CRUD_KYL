package com.vti.testing.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vti.testing.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// List ROLE của user(1 user có thể có nhiều role nên mặc định sẽ là list)
		List<SimpleGrantedAuthority> auth = new ArrayList<>();
		// Get role user -> add to List
		auth.add(new SimpleGrantedAuthority(user.getRole()));
		return auth;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassWord();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
