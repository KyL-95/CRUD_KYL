package com.vti.testing.controller;

import java.util.List;

import com.vti.testing.author_anotations.IsAdmin;
import com.vti.testing.author_anotations.IsUser;
import com.vti.testing.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.vti.testing.formcreate.FormUserCreate;
import com.vti.testing.responseobj.ResponseObj;
import com.vti.testing.service.interfaces.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private IUserService userService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@GetMapping("/getAll")
//	@PreAuthorize("hasAnyRole('MANAGER')")
// 	@IsManager  // == dòng 36
	public List<UserDTO> getAll() {
		return userService.getAllUsers();
	}
	@GetMapping("/getAllActiveUser")
//	@IsAdmin
	public List<UserDTO> getAllActiveUser() {
		return userService.getAllActiveUser();
	}
	@GetMapping("/getById/{id}")
	public UserDTO getById(@PathVariable("id") int id){
		return userService.getById(id);
	}
	@PostMapping("/newUser")
//	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	public ResponseObj newUser(@RequestBody(required = true) FormUserCreate newUser) throws Exception {
		return userService.newUser(newUser);
	}

	@PatchMapping(path = "/updatePassWord/{id}",consumes = "application/json")
	public ResponseObj updatePassWord(@RequestBody(required = true) FormUserCreate user,
			@PathVariable("id") int id) {
		String newPassEncode = passwordEncoder.encode(user.getPassWord());
		userService.updatePassWord(id, newPassEncode);
		return new ResponseObj("200", "PassWord has been Updated", newPassEncode);
	}
	@DeleteMapping("/delete/{id}")
	public ResponseObj deleteUser(@PathVariable("id") int id) {
		return userService.deleteUser(id);
	}
}
