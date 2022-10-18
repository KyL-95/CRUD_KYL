package com.vti.testing.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Mapper {

	@Bean
	public ModelMapper initMapper() {
		return new ModelMapper();
	}
}
