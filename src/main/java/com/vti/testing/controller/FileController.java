package com.vti.testing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {
	
	@PostMapping("/uploadFile")
	public void uploadFile(MultipartFile file) {
		
	}

}
