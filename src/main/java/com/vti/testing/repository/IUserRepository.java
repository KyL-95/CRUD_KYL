package com.vti.testing.repository;

import com.vti.testing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer>{
	 User findByUserName(String userName);
	 boolean existsByUserName(String userName);
	 @Query("SELECT u from User u WHERE u.active = '1'")
	 List<User> getAllActiveUser();

}
