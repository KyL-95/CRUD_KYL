package com.vti.testing.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`role`")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId", nullable = false)
    private int roleId;
    private String roleName;
    @ManyToMany(cascade = { CascadeType.ALL },mappedBy = "roles",fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<User>();

    @Override
    public String toString() {
        return  roleName ;
    }
}
