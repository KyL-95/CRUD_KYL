package com.vti.testing.service;

import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.Role;
import com.vti.testing.entity.User;
import com.vti.testing.repository.IRoleRepository;
import com.vti.testing.repository.IUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class AuthService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;

    public User getUserByName (String email){
        if(!userRepository.existsByUserName(email)){
            String DEFAULT_PASSWORD = "Superkyl2509";
            User newUser = new User(email, DEFAULT_PASSWORD);
            newUser.setActive("1");
            Role role = roleRepository.findByRoleName("ROLE_USER");
            List<Role> newUserRoles = new ArrayList<Role>();
            newUserRoles.add(role);
            newUser.setRoles(newUserRoles);
            userRepository.save(newUser);
            return newUser;
        }
        return userRepository.findByUserName(email);
    }
}
