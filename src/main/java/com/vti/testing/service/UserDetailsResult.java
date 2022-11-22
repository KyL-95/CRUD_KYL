package com.vti.testing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.vti.testing.entity.User;
import com.vti.testing.repository.IUserRepository;

@Component
public class UserDetailsResult implements UserDetailsService{
	@Autowired
	private IUserRepository userRepository;

	@Override
	public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		User user = userRepository.findByUserName(userName);
		if(user == null) {
			throw new UsernameNotFoundException("This user:'" + userName + "' does not exists!");
		}
		System.out.println("user: "+ user);
		return new CustomUserDetails(user);
	}

}
