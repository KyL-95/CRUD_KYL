package com.vti.testing.oauth2;
import com.vti.testing.entity.Role;
import com.vti.testing.entity.User;
import com.vti.testing.repository.IRoleRepository;
import com.vti.testing.repository.IUserRepository;
import com.vti.testing.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class RestFB {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;

    public UserDetails buildUser(String email) {
        String userName = email.substring(0, email.indexOf("@")); // lấy email bỏ @gmail....
        String DEFAULT_PASSWORD = "Superkyl2509";
        String DEFAULT_ROLE = "ROLE_USER";
        if(userRepository.existsByUserName(userName)) {
            System.out.println("This user is exists");
            com.vti.testing.entity.User userEntity = userRepository.findByUserName(userName);
            UserDetails details = new CustomUserDetails(userEntity);
            return details;
        }

        com.vti.testing.entity.User newUser = new User(userName,DEFAULT_PASSWORD);
        newUser.setActive("1");

        Role role = roleRepository.findByRoleName(DEFAULT_ROLE);
        List<Role> newUserRoles = new ArrayList<Role>();
        newUserRoles.add(role);
        newUser.setRoles(newUserRoles);

        userRepository.save(newUser);
        UserDetails details = new CustomUserDetails(newUser);
        return details;
    }
}
