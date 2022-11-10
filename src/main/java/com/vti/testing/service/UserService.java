package com.vti.testing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.Role;
import com.vti.testing.entity.User;
import com.vti.testing.exception.custom_exception.NotFoundEx;
import com.vti.testing.formcreate.FormUserCreate;
import com.vti.testing.repository.IRoleRepository;
import com.vti.testing.repository.IUserRepository;

import com.vti.testing.responseobj.ResponseObj;
import com.vti.testing.service.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class UserService implements IUserService {

	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IRoleRepository roleRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
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

	@Override
	public ResponseObj newUser(FormUserCreate newUser) {
		// Encode passWord
		String passEncode = passwordEncoder.encode(newUser.getPassWord());
		newUser.setPassWord(passEncode);

		User user = new User(newUser.getUserName(), newUser.getPassWord());
		Role role = roleRepository.findByRoleName("ROLE_USER");
		List<Role> newUserRoles = new ArrayList<Role>();
			newUserRoles.add(role); // role default = ROLE_USER
		user.setRoles(newUserRoles);
		user.setActive("1");
		userRepository.save(user);
		return new ResponseObj("200", "Created New User", user.getUserName());
	}

	@Override
	public List<UserDTO> getAllUsers() {
		List<User> entities = userRepository.findAll();

		List<UserDTO> dtos = modelMapper.map(entities,
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
	public ResponseObj deleteUser(int id) {
		if (userRepository.existsById(id)){
			String userNameDelete = userRepository.findById(id).get().getUserName();
			userRepository.deleteById(id);
			return new ResponseObj("200", "Delete Success !", userNameDelete);
		}
		throw new NotFoundEx("User is not exists with this id");

	}

}
