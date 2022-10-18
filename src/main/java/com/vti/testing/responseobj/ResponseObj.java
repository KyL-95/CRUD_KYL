package com.vti.testing.responseobj;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ResponseObj {


	private String status;
	private String message;
	private Object data;

	public ResponseObj(String status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
}
