package com.vti.testing;

import com.vti.testing.bosung.eventlistener.NhaHangXom;
import com.vti.testing.jwt.JwtProperties;
import com.vti.testing.repository.IUserRepository;
import com.vti.testing.service.UserService;
import com.vti.testing.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.vti.testing")
@EnableScheduling
@EnableAsync
@EnableCaching
//@PropertySource("classpath:application-dev.properties")
//@PropertySource("classpath:application-common.properties")
public class CrudKylApplication implements CommandLineRunner {
    @Autowired
    private JwtProperties properties;
    @Value("${jwt.refreshExpiration}")
    private Long expiration;
    @Autowired
    private Environment env;
    @Autowired
    NhaHangXom nhaHangXom;

    public static void main(String[] args) {
        ApplicationContext context =  SpringApplication.run(CrudKylApplication.class, args);
//        System.out.println(context.getBean(JwtProperties.class));
//        System.out.println(context.getBean(JwtProperties.class));

    }
    @Override
    public void run(String... args){
//        System.out.println("Expiration use @ConfigurationProperties :" + properties.getRefreshExpiration());
//        System.out.println("Expiration use @value: " + expiration) ;
//        System.out.println("Expiration use evn obj : " + env.getProperty("jwt.refreshExpiration")) ;
//        nhaHangXom.tromCho();
    }
}
