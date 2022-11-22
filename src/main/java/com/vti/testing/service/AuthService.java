package com.vti.testing.service;

import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.Role;
import com.vti.testing.entity.User;
import com.vti.testing.repository.IRoleRepository;
import com.vti.testing.repository.IUserRepository;
import com.vti.testing.responseobj.ResponseObj;
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
    @Autowired
    private ModelMapper modelMapper;

    public ResponseObj getUserByName (String email){
        if(!userRepository.existsByUserName(email)){
            String DEFAULT_PASSWORD = "Superkyl2509";
            User newUser = new User(email, DEFAULT_PASSWORD);
            newUser.setActive("1");
            Role role = roleRepository.findByRoleName("ROLE_USER");
            List<Role> newUserRoles = new ArrayList<Role>();
            newUserRoles.add(role);
            newUser.setRoles(newUserRoles);
            userRepository.save(newUser);
            UserDTO dto = modelMapper.map(newUser, UserDTO.class);
            return new ResponseObj(""," New User has been created",dto);
        }
        return new ResponseObj("","This user has been existed",
                modelMapper.map(userRepository.findByUserName(email), UserDTO.class));
    }
}
