package com.vti.testing.repository;

import com.vti.testing.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer>{
	 User findByUserName(String userName);
	 boolean existsByUserName(String userName);
	 @Query("SELECT u from User u WHERE u.active = '1'")
	 Page<User> getAllActiveUser(Pageable pageable);
	 @Query(value = "SELECT * FROM User u WHERE u.active = '0'",nativeQuery = true)
	 Slice<User> getNoActiveUser (Pageable pageable );
	 @Query(value="SELECT * FROM `user`As u INNER JOIN user_role AS ur ON u.userId = ur.userId " +
			 "INNER JOIN role as r ON ur.roleId = r.roleId WHERE r.roleId = ?1", nativeQuery = true)
	 List<User> getByRoleId(Integer roleId);





}
