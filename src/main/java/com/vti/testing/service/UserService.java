package com.vti.testing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.Role;
import com.vti.testing.entity.User;
import com.vti.testing.exception.custom_exception.AlreadyExistEx;
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
	private final String PASSWORD_PATTERN =
			"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
	private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
	public boolean validatePassWord(String password) {
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

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
		if(userRepository.existsByUserName(userName)){
			return userRepository.findByUserName(userName);
		}
		throw new NotFoundEx("This user: " + userName + " is not found!");
	}

	@Override
	public UserDTO getUserById(int id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			UserDTO dto = modelMapper.map(user.get(), UserDTO.class);
			return dto;
		}
		throw new NotFoundEx("Cannot find user with id = " + id );
	}

	@Override
	public ResponseObj newUser(FormUserCreate newUser) throws Exception {
		String userName = newUser.getUserName();
		String passWord = newUser.getPassWord();
		// Validate user name
		if(userRepository.existsByUserName(userName)){
			throw new AlreadyExistEx("This user: " + userName + " has been used!");
		}
		// Validate user's password :
		if (!validatePassWord(passWord)){
			throw new Exception("Password is not valid, please check your password");
		}
		// Encode passWord
		String passEncode = passwordEncoder.encode(passWord);
		newUser.setPassWord(passEncode);
		User user = new User(userName, newUser.getPassWord());
		Role role = roleRepository.findByRoleName("ROLE_USER");
		List<Role> newUserRoles = new ArrayList<Role>();
			newUserRoles.add(role); // role default = ROLE_USER
		user.setRoles(newUserRoles);
		user.setActive("1");
		userRepository.save(user);
		return new ResponseObj("200", "New User has been created", userName);
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
		throw new NotFoundEx("This user is not exists with to delete");

	}

}
