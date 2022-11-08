package com.vti.testing.controller;

import java.security.Principal;
import java.util.List;

import com.vti.testing.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vti.testing.entity.User;
import com.vti.testing.formcreate.FormUserCreate;
import com.vti.testing.responseobj.ResponseObj;
import com.vti.testing.service.IUserService;

@RestController
public class UserController {
	@Autowired
	private IUserService userService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	AuthenticationManager authenticationManager;
	@GetMapping("/getAll")
//	@PreAuthorize("hasAnyRole('MANAGER')")
// 	@IsManager  // == dòng 36
	public List<UserDTO> getAll() {
		return userService.getAllUsers();
	}
//	@GetMapping("/logining-user")	// Lấy thông tin user đang login : Dùng principal
////	@PreAuthorize("hasAnyRole('ADMIN')")  // Admin mới đc gọi API này
//	public UserDTO loginInfor(Principal principal) {
//		String loginUserName = principal.getName();
//		System.out.println("Logining : " + loginUserName);
//		User entity = userService.getByUserName(loginUserName);
//		System.out.println(loginUserName);
//		return modelMapper.map(entity, UserDTO.class);
//
//	}
	
//	@PostMapping("/newUser")
//	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
//	public ResponseObj newUser(@RequestBody(required = true) FormUserCreate newUser) {
//		// convert password from String -> Encode
//		String passEncode = passwordEncoder.encode(newUser.getPassWord());
//		newUser.setPassWord(passEncode);
//
//		userService.newUser(newUser);
//		return new ResponseObj("200", "Created New User", newUser);
//
//	}

	@PatchMapping(path = "/updatePassWord/{id}",consumes = "application/json")
	public ResponseObj updatePassWord(@RequestBody(required = true) FormUserCreate user,
			@PathVariable("id") int id) {
		String newPassEncode = passwordEncoder.encode(user.getPassWord());
		userService.updatePassWord(id, newPassEncode);
		return new ResponseObj("200", "PassWord has been Updated", newPassEncode);

	}
	
	@DeleteMapping("delete/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseObj deleteUser(@PathVariable("id") int id) {
	userService.deleteUser(id);
		return new ResponseObj("200", "Delete Success !", "");
	}
}
