package com.vti.testing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.vti.testing.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UserDTO {
	@JsonProperty("ID")
	private int userId;
	private String userName;
	private String active;
	@NonNull
	private List<String> roles;

	public UserDTO(int userId, String userName, String active, List roles) {
	}
}
