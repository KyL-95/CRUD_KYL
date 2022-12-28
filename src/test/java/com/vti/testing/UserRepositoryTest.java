package com.vti.testing;

import com.vti.testing.entity.User;
import com.vti.testing.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private IUserRepository userRepository;
    @Test
    public void testGetAllUser(){
        List<User> users = userRepository.findAll();
        assertEquals(5,users.size());
    }
    @Test
    public void testFindByUserName(){
        User user = userRepository.findByUserName("kyl01");
        assertNotNull(user);
    }
    @Test
    public void testGetAllActiveUser(){ // 01-04
        Pageable pageable = PageRequest.of(0,5);
        Page<User> users = userRepository.getAllActiveUser(pageable);
        List<User> userList = users.getContent();
        assertTrue(userList.stream().filter(u->u.getUserName().equals("kyl01")).count() == 1);
        assertFalse(userList.stream().filter(u->u.getUserName().equals("kyl03")).count() == 1);
        assertEquals(2,userList.size());
    }
    @Test
    public void testGetByRoleId(){
        List<User> userByRoles =  userRepository.getByRoleId(2);
        System.out.println(userByRoles);
        assertEquals(2,userByRoles.size());
    }
    @Test
    public void testSaveUser(){
        User user = getUser();
        User userSave = userRepository.save(user);
        assertNotNull(userSave);
    }
    private User getUser() {
        User user = new User();
        user.setUserName("Khanh");
        user.setPassWord("Superkyl2509");
        return user;
    }

}
