package com.vti.testing.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
@Configuration
@Scope("prototype")
//@Profile("dev")
public class JwtProperties {
    private String secret;
    private Long expiration;
    private Long refreshExpiration;
//    private List<Integer> numbers;

//    @PostConstruct
//    public void post(){
//        System.out.println(" After init JwtProperties beans");
//    }
//    @PreDestroy
//    public void destroy(){
//        System.out.println("Before destroy JwtProperties beans");
//    }
}
