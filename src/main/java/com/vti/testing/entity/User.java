package com.vti.testing.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "`user`")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String userName;
	private String passWord;
	private String active;
	@OneToOne(mappedBy = "user",fetch = FetchType.LAZY)
	private RefreshToken refreshToken;

	@ManyToMany
			(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_role",
			joinColumns = { @JoinColumn(name = "userId") },
			inverseJoinColumns = { @JoinColumn(name = "roleId") }
	)
	List<Role> roles = new ArrayList<>();
	public User(String userName, String passWord) {
		this.userName = userName;
		this.passWord = passWord;
	}

	@Override
	public String toString() {
		return
				"Name=" + userName ;
	}
}
