package com.vti.testing.formcreate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FormUserCreate {
	private String userName;
	private String passWord;
	private String active;
	private String role;

}
