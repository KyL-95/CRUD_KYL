package com.vti.testing.service;

import java.util.ArrayList;
import java.util.List;

import com.vti.testing.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetails implements UserDetailsService{
	@Autowired
	private UserService userService;

	@Override
	public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		User user = userService.getByUserName(userName);
		if(user == null) {
			throw new UsernameNotFoundException("This user:'" + userName + "' does not exists!");
		}
		// List ROLE của user(1 user có thể có nhiều role nên mặc định sẽ là list)
		List<SimpleGrantedAuthority> auth = new ArrayList<>();
		// Get role user -> add to List		
		auth.add(new SimpleGrantedAuthority(user.getRole()));
		
		org.springframework.security.core.userdetails.User details = new org.springframework.security.core.userdetails
				.User(user.getUserName(), user.getPassWord(), auth);
		
		return details;
	}

}
