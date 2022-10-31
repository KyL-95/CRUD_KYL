package com.vti.testing.service;

import java.util.List;

import com.vti.testing.entity.User;
import com.vti.testing.formcreate.FormUserCreate;
import com.vti.testing.repository.IUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService implements IUserService{

	@Autowired
	private IUserRepository userRepository;
	
	@Override
	public User getByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	@Override
	public void newUser(FormUserCreate newUser) {
		User user = new User(newUser.getUserName(), newUser.getPassWord(), newUser.getActive(), newUser.getRole());
		userRepository.save(user);
	
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public void updatePassWord(int id, String newPass) {
			User userUpdate = userRepository.findById(id).get();
			userUpdate.setPassWord(newPass);
			userRepository.save(userUpdate);
			
	}

	@Override
	public void deleteUser(int id) {
			userRepository.deleteById(id);
	}

}
