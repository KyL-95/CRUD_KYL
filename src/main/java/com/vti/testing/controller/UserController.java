package com.vti.testing.controller;

import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vti.testing.author_anotations.IsManager;
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
	
	@GetMapping("/getAll")
//	@PreAuthorize("hasAnyRole('MANAGER')")
 	@IsManager  // == dòng 34
	public List<User> getAll() {
		return userService.getAllUsers();
	}

	@PostMapping("/user-login")
	public String login() {
		return "Login success";
	}

	@GetMapping("/logining-user")	// Lấy thông tin user đang login : Dùng principal
//	@PreAuthorize("hasAnyRole('ADMIN')")  // Admin mới đc gọi API này
	public User loginInfor(Principal principal) {
		System.out.println(principal.getName());
		return userService.getByUserName(principal.getName());

	}
	
	@PostMapping("/newUser")
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	public ResponseObj newUser(@RequestBody(required = true) FormUserCreate newUser) {
		// convert password from String -> Encode
		String passEncode = passwordEncoder.encode(newUser.getPassWord());
		newUser.setPassWord(passEncode);

		userService.newUser(newUser);
		return new ResponseObj("200", "Created New User", newUser);

	}

	@PatchMapping("/updatePassWord/{id}")
	public ResponseObj updatePassWord(@RequestBody(required = true) FormUserCreate user,
			@PathVariable("id") int id) {
		String newPassEncode = passwordEncoder.encode(user.getPassWord());
		userService.updatePassWord(id, newPassEncode);
		return new ResponseObj("200", "PassWord was Updated", newPassEncode);

	}
	
	@DeleteMapping("delete/{id}")
	public ResponseObj deleteUser(@PathVariable("id") int id) {
	userService.deleteUser(id);
		return new ResponseObj("200", "Delete Success !", "");
	}
}
