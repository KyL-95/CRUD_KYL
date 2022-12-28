package com.vti.testing;

import com.vti.testing.entity.User;
import com.vti.testing.exception.custom_exception.NotFoundEx;
import com.vti.testing.repository.IUserRepository;
import com.vti.testing.service.UserService;
import com.vti.testing.service.interfaces.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    IUserRepository userRepository ;// = Mockito.mock(IUserRepository.class);
    @InjectMocks
    UserService userService;
    @Test
    public void testGetByUserNameEx(){
        when(userRepository.existsByUserName("kyl01")).thenReturn(false);
        assertThrows(NotFoundEx.class,()->{
            userService.getByUserName("kyl01");
        });
    }

    @Test
    public void testGetByUserName(){
        when(userRepository.findByUserName("UserName")).thenReturn(new User("UserName","Password"));
        when(userRepository.existsByUserName("UserName")).thenReturn(true);
        User user = userService.getByUserName("UserName");
        assertEquals("Password",user.getPassWord());
        assertNotNull(user);
    }

    @Test
    public void testDeleteById(){
        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.existsById(2)).thenReturn(false);
        String del = userService.deleteUser(1);
        assertNotNull(del);
        System.out.println(del);
        assertThrows(NotFoundEx.class,()->{
            userService.deleteUser(2);
        });

    }
    @Test
    public void testGetAll(){
        userService.getAllUsers();
        verify(userRepository).findAll();
    }



}
