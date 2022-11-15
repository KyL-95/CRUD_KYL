package com.vti.testing.service.interfaces;

import java.util.List;

import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.User;
import com.vti.testing.formcreate.FormUserCreate;
import com.vti.testing.responseobj.ResponseObj;

public interface IUserService  {

	User getByUserName(String userName);

	UserDTO getUserById(int id);
	ResponseObj newUser(FormUserCreate newUser) throws Exception;

	List<UserDTO> getAllUsers();
	List<UserDTO> getAllActiveUser();

	void updatePassWord(int id, String newPass);

	ResponseObj deleteUser(int id);

    UserDTO getById(int id);
}
