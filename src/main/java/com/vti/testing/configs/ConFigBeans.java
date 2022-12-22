package com.vti.testing.configs;

import com.vti.testing.email.EmailProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Properties;

@Configuration
@EnableAsync
public class ConFigBeans {
	@Autowired
	EmailProperties emailProperties;
	@Bean
//	@Primary
	public ModelMapper initMapper() {
		return new ModelMapper();
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public WebClient webClient() {
		return WebClient.create("http://hrm-api.nccsoft.vn/api/services/app/CheckIn/GetUserForCheckIn");
	}
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(emailProperties.getHost());
		mailSender.setPort(emailProperties.getPort());
		mailSender.setUsername(emailProperties.getUserName());
		mailSender.setPassword(emailProperties.getPassWord());

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.debug", "true");

		return mailSender;
	}







}