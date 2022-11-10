package com.vti.testing.service;

import com.vti.testing.dto.RoleDTO;
import com.vti.testing.dto.UserDTO;
import com.vti.testing.entity.Role;
import com.vti.testing.repository.IRoleRepository;
import com.vti.testing.responseobj.ResponseObj;
import com.vti.testing.service.interfaces.IRoleService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ResponseObj getAllRoles() {
        List<Role> rolesEntity = roleRepository.findAll();
        List<RoleDTO> rolesDto = modelMapper.map(rolesEntity,
                new TypeToken<List<RoleDTO>>(){}.getType());
        return new ResponseObj("200", "Successfully!", rolesDto);
    }
}
