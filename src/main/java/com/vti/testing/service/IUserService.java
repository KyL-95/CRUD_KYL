package com.vti.testing.service;

import java.util.List;

import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.User;

public interface IUserService  {

	User getByUserName(String userName);

	UserDTO getUserById(int id);
//	void newUser(FormUserCreate newUser);

	List<UserDTO> getAllUsers();

	void updatePassWord(int id, String newPass);

	void deleteUser(int id);

}
