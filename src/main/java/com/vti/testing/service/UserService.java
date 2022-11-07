package com.vti.testing.service;

import java.util.List;
import java.util.Optional;

import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.User;
import com.vti.testing.exception.custom_exception.NotFoundEx;
import com.vti.testing.repository.IUserRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService implements IUserService {

	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public User getByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	@Override
	public UserDTO getUserById(int id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			UserDTO dto = modelMapper.map(user.get(), UserDTO.class);
			return dto;
		}
		throw new NotFoundEx("Cannot find user with this id " + id );
	}

//	@Override
//	public void newUser(FormUserCreate newUser) {
//		User user = new User(newUser.getUserName(), newUser.getPassWord(), newUser.getActive(), newUser.getRole());
//		userRepository.save(user);
//
//	}

	@Override
	public List<UserDTO> getAllUsers() {
		List<User> entitys = userRepository.findAll();
		List<UserDTO> dtos = modelMapper.map(entitys,
				new TypeToken<List<UserDTO>>(){}.getType());
		return dtos;
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
