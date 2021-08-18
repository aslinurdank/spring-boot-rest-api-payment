package com.example.CodeTest.controller;

import com.example.CodeTest.model.MessageType;
import com.example.CodeTest.model.Origin;
import com.example.CodeTest.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionController transactionController;

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetAllTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(new Transaction(MessageType.PAYMENT, 4755L, Origin.MASTER, new BigDecimal(30.00)));
        Mockito.when(transactionController.getAllTransactions()).thenReturn(new ResponseEntity<>(transactions, HttpStatus.OK));
        mockMvc.perform(get("/api/transactions")).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(1))).andExpect(jsonPath("$[0].accountId", is(4755)));
    }

    @Test
    public void testCreateTransaction() throws Exception {

        Transaction transaction =new Transaction(MessageType.PAYMENT, 4755L, Origin.VISA, new BigDecimal(30.00));

        Mockito.when(transactionController.createTransaction(ArgumentMatchers.any())).thenReturn(new ResponseEntity<>(transaction, HttpStatus.CREATED));
        String json = mapper.writeValueAsString(transaction);

        mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId", Matchers.equalTo(4755)))
                .andExpect(jsonPath("$.messageType", Matchers.equalTo("PAYMENT")))
                .andExpect(jsonPath("$.origin", Matchers.equalTo("VISA")))
                .andExpect(jsonPath("$.amount", Matchers.equalTo( 30)));
    }

    @Test
    public void testUpdateTransaction()   throws Exception {
        UUID id=UUID.randomUUID();
        Transaction transaction =new Transaction(MessageType.PAYMENT, 4755L, Origin.VISA, new BigDecimal(30.00));
        transaction.setTransactionId(id);

        Mockito.when(transactionController.updateTransaction(ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(new ResponseEntity<>(transaction, HttpStatus.OK));
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/api/transactions/" + id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(getTransactionInJson(id));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(jsonPath("$.accountId", Matchers.equalTo(4755)))
                .andExpect(jsonPath("$.messageType", Matchers.equalTo("PAYMENT")))
                .andExpect(jsonPath("$.origin", Matchers.equalTo("VISA")))
                .andExpect(jsonPath("$.amount", Matchers.equalTo( 30)));
    }

    private String getTransactionInJson(UUID id) {
        return "{\"id\":\"" + id + "\", \"content\":\"test data\"}";
    }

}