package com.vti.testing.formcreate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormUserCreate {
	private String userName;
	private String passWord;
//	private String active;
//	private List<String> roles;

}
