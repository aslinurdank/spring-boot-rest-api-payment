package com.example.CodeTest.controller;

import com.example.CodeTest.model.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountController accountController;

    private static ObjectMapper mapper = new ObjectMapper();
    @Test
    public void testGetAccountById() throws Exception {
        Long id=4755L;
        List<Account> accounts = new ArrayList<Account>();
        Account accountFoundById=new Account(4755L,  new BigDecimal(1001.88));
        Mockito.when(accountController.getAccountById(id)).thenReturn( new ResponseEntity<>(accountFoundById, HttpStatus.OK));
        mockMvc.perform(get("/api/accounts/"+id)).andExpect(status().isOk()).andExpect(jsonPath("$.accountId", is(4755)));
    }

}