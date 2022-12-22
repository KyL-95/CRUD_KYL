package com.vti.testing.service;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vti.testing.bosung.resttemplate.ResultsList;
import com.vti.testing.dto.UserDTO;
import com.vti.testing.bosung.resttemplate.Result;
import com.vti.testing.entity.Role;
import com.vti.testing.entity.User;
import com.vti.testing.exception.custom_exception.AlreadyExistEx;
import com.vti.testing.exception.custom_exception.NotFoundEx;
import com.vti.testing.formcreate.FormUserCreate;
import com.vti.testing.repository.IRoleRepository;
import com.vti.testing.repository.IUserRepository;
import com.vti.testing.responseobj.ResponseObj;
import com.vti.testing.service.interfaces.IUserService;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@Getter
@Scope("prototype")
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
	public ResponseObj newUser(FormUserCreate newUser) throws Exception {
		String userName = newUser.getUserName();
		String passWord = newUser.getPassWord();
		if(userRepository.existsByUserName(userName)){
			throw new AlreadyExistEx("This user: " + userName + " has been used!");
		}
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
		TypeMap<User,UserDTO> typeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
		typeMap.addMapping(User::getUserName, UserDTO::setUserName);

		List<User> entityList = userRepository.findAll();
		List<UserDTO> dtoList = modelMapper.map(entityList,new TypeToken<List<UserDTO>>(){}.getType());
		return dtoList;
	}

	@Override
	public List<UserDTO> getByRoleId(Integer roleId) {
		List<User> entities = userRepository.getByRoleId(roleId);
		return modelMapper.map(entities,
				new TypeToken<List<UserDTO>>(){}.getType());

	}

	@Override
	public void updatePassWord(int id, String newPass) {
			User userUpdate = userRepository.findById(id).get();
			String newPassEncode = passwordEncoder.encode(newPass);
			userUpdate.setPassWord(newPassEncode);
			userRepository.save(userUpdate);

	}

	@Override
	@Transactional
	public String deleteUser(int id) {
		if (userRepository.existsById(id)){
			userRepository.deleteById(id);
			return "Delete Successfully";
		}
		throw new NotFoundEx("This user is not exists to delete");
	}

	@Override
	public UserDTO getById(int id) {
		if(!userRepository.existsById(id)){
			throw new AlreadyExistEx("This user is not exists");
		}
		return modelMapper.map(userRepository.findById(id).get(), UserDTO.class);
	}

	@Override
	public Flux<Result> getUserNCC() {
		WebClient webClient = WebClient.create("http://hrm-api.nccsoft.vn");
		Flux<Result> users = webClient.get() //method
				.uri("/api/services/app/CheckIn/GetUserForCheckIn") //uri
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) //
				.retrieve()
				.bodyToFlux(Result.class);
		return users;
	}
	@Override
	public Slice<UserDTO> getNoActiveUser(Pageable pageable) {
		Slice<User> entitySlice = userRepository.getNoActiveUser(pageable);
		List<User> entityList = entitySlice.getContent();
		List<UserDTO> dtoList = modelMapper.map(entityList,new TypeToken<List<UserDTO>>(){}.getType());
		Slice<UserDTO> dtoSlice = new SliceImpl<>(dtoList);
		return dtoSlice;
	}
	@Override
	public Page<UserDTO> getAllActiveUser(Pageable pageable) {
//		Sort sort = Sort.by("userName").descending();
//		pageable = PageRequest.of(0,3,sort);
		Page<User> entityPage = userRepository.getAllActiveUser(pageable);
		List<User> entityList = entityPage.getContent();
		List<UserDTO> dtoList = modelMapper.map(entityList,new TypeToken<List<UserDTO>>(){}.getType());
		Page<UserDTO> dtoPage = new PageImpl<>(dtoList,pageable,entityPage.getTotalElements());
		return dtoPage;
	}

	public ResultsList getNccUsers() throws JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();
		String requestURL
				= "http://hrm-api.nccsoft.vn/api/services/app/CheckIn/GetUserForCheckIn";

		ResponseEntity<String> response
				= restTemplate.getForEntity(requestURL, String.class);
		ObjectMapper om = new ObjectMapper();
		ResultsList result = om.readValue(response.getBody(), ResultsList.class);

		System.out.println(result.getResult());
		return result;
	}


}