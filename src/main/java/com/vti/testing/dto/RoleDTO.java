package com.vti.testing.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoleDTO {
    private String roleName;
    private List<String> users;

}
