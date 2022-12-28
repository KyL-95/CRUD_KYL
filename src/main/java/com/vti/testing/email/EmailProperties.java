package com.vti.testing.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "config.mail")
@Getter
@Setter
@Configuration
public class EmailProperties {
    private String host;
    private int port;
    private String userName;
    private String passWord;

}
