package com.vti.testing.formcreate;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FormUserCreate {
	private String userName;
	private String passWord;
	private String active;
//	private List<String> roles;

}
