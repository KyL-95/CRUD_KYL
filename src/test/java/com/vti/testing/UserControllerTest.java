package com.vti.testing;

import com.vti.testing.dto.UserDTO;
import com.vti.testing.service.interfaces.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IUserService userService ;
    private List<UserDTO> activeUsers = new ArrayList<>();

    @BeforeEach
    public void setup(){
        UserDTO dto1 = new UserDTO(1,"kyl001","1", Collections.EMPTY_LIST);
        UserDTO dto2 = new UserDTO(2,"kyl002","1", Collections.EMPTY_LIST);
        UserDTO dto3 = new UserDTO(3,"kyl003","0", Collections.EMPTY_LIST);
        UserDTO dto4 = new UserDTO(4,"kyl004","1", Collections.EMPTY_LIST);
        activeUsers.add(dto1);
        activeUsers.add(dto2);
        activeUsers.add(dto3);
        activeUsers.add(dto4);
        System.out.println("--------- User Controller Test ---------");
    }
    @Test
    public void testGetAllUser() throws Exception{
        given(userService.getAllUsers()).willReturn(activeUsers);
        this.mockMvc.perform(get("/user/getAllActiveUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4))) ;
    }

    @Test
    public void testDeleteUser() throws Exception{
        when(userService.deleteUser(1)).thenReturn("Successfully rồi!");
        MvcResult result = mockMvc.perform((delete("/user/delete/{id}",1)))
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("____________-------------------" + content);
        assertEquals("Successfully rồi!",content);
    }

}

