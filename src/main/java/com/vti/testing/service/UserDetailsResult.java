package com.vti.testing.service;

import com.vti.testing.entity.User;
import com.vti.testing.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsResult implements UserDetailsService{

	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		User user = userRepository.findByUserName(userName);
		if(user == null) {
			System.out.println("This user '" + userName + "' does not exists!");
			throw new UsernameNotFoundException("This user '" + userName + "' does not exists!");
		}
		return new CustomUserDetails(user);
	}

	public boolean checkPassWord(String userName,String passWord){
		User user = userRepository.findByUserName(userName);
		return passwordEncoder.matches(passWord,user.getPassWord());
	}

}
