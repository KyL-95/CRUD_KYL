package com.vti.testing.controller;

import com.vti.testing.responseobj.ResponseObj;
import com.vti.testing.service.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Value("${jwt.JWT_SECRET}")
    private String jwtSecret;
    @Autowired
    private IRoleService rolesService;
    @GetMapping("/getAll")
    public ResponseObj getAllRoles(){
        System.out.println(jwtSecret);
        return rolesService.getAllRoles();
    }

}
