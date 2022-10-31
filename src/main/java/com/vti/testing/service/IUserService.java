package com.vti.testing.service;

import java.util.List;

import com.vti.testing.entity.User;
import com.vti.testing.formcreate.FormUserCreate;

public interface IUserService {

	User getByUserName(String userName);

	void newUser(FormUserCreate newUser);

	List<User> getAllUsers();

	void updatePassWord(int id, String newPass);

	void deleteUser(int id);

}
