package com.vti.testing.service.interfaces;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vti.testing.bosung.resttemplate.ResultsList;
import com.vti.testing.dto.UserDTO;
import com.vti.testing.bosung.resttemplate.Result;
import com.vti.testing.entity.User;
import com.vti.testing.formcreate.FormUserCreate;
import com.vti.testing.responseobj.ResponseObj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import reactor.core.publisher.Flux;

public interface IUserService  {
	User getByUserName(String userName);
	ResponseObj newUser(FormUserCreate newUser) throws Exception;
	List<UserDTO> getAllUsers();
	Page<UserDTO> getAllActiveUser(Pageable pageable);
	Slice<UserDTO> getNoActiveUser(Pageable pageable);
	List<UserDTO> getByRoleId(Integer roleId);
	void updatePassWord(int id, String newPass);
	String deleteUser(int id);
    UserDTO getById(int id);
	Flux<Result> getUserNCC();
	ResultsList getNccUsers() throws JsonProcessingException;

}
