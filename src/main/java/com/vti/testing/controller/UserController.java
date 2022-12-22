package com.vti.testing.controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vti.testing.author_anotations.IsManager;
import com.vti.testing.bosung.resttemplate.ResultsList;
import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.vti.testing.formcreate.FormUserCreate;
import com.vti.testing.responseobj.ResponseObj;
import com.vti.testing.service.interfaces.IUserService;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	WebClient webClient;
	@Autowired
	private IUserService userService;
//	@Autowired
//	AuthenticationManager authenticationManager;
	@GetMapping("/getAll")
//	@PreAuthorize("hasAnyRole('MANAGER')")
 	@IsManager  // == dòng 36
	public List<UserDTO> getAll() {
		return userService.getAllUsers();
	}
	@GetMapping("/getAllActiveUser")
	public Page<UserDTO> getAllActiveUser(Pageable pageable) {
		return userService.getAllActiveUser(pageable);
	}
	@GetMapping("/getNoActiveUser")
	public Slice<UserDTO> getNoActiveUser(Pageable pageable) {
		return userService.getNoActiveUser(pageable);
	}
	@GetMapping("/getById/{id}")
	public UserDTO getById(@PathVariable("id") int id){
		return userService.getById(id);
	}

	@GetMapping("/getByRoleId/{roleId}")
	public List<UserDTO> getByRoleId(@PathVariable("roleId") int roleId){
		return userService.getByRoleId(roleId);
	}
	@PostMapping("/newUser")
//	@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
	public ResponseObj newUser(@RequestBody(required = true) FormUserCreate newUser) throws Exception {
		return userService.newUser(newUser);
	}

	@PatchMapping(path = "/updatePassWord/{id}",consumes = "application/json")
	public ResponseObj updatePassWord(@RequestBody(required = true) FormUserCreate user,
			@PathVariable("id") int id) {
		userService.updatePassWord(id, user.getPassWord());
		return new ResponseObj("200", "PassWord has been Updated", " ");

	}
//	@Transactional
//			(noRollbackFor = ArithmeticException.class)
	@DeleteMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") int id) {
		String del = userService.deleteUser(id);
		return del;
	}
	@GetMapping("/getByUserName")
	public User getByUserName(@RequestParam("userName") String userName){
		return userService.getByUserName(userName);
	}

	@GetMapping("/client-ncc1")
	public ResultsList findAll() throws JsonProcessingException {
		return userService.getNccUsers();
	}

}
