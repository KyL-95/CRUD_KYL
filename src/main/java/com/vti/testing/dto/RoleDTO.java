package com.vti.testing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoleDTO {
    @JsonProperty("ID")
    private int roleId;
    private String roleName;
    private List<String> users;

}
