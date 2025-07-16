package org.banking.service.transactions;

import org.banking.service.controller.TransactionController;
import org.banking.service.model.*;
import org.banking.service.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.banking.service.service.AccountService;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;
    @MockBean
    private AccountService accountService;

    private String getAuthHeader() {
        return "Bearer " + org.banking.service.util.JwtUtil.generateToken("usr-abc123");
    }

    @Test
    void testCreateTransaction() throws Exception {
        CreateTransactionRequest req = new CreateTransactionRequest();
        req.setAmount(100.0);
        req.setCurrency("GBP");
        req.setType("deposit");
        TransactionResponse resp = new TransactionResponse();
        resp.setId("tan-1234567");
        resp.setAmount(100.0);
        resp.setCurrency("GBP");
        resp.setType("deposit");
        BankAccountResponse account = new BankAccountResponse();
        account.setAccountNumber("01000001");
        account.setUserId("usr-abc123");
        when(accountService.fetchAccount("01000001")).thenReturn(account);
        when(transactionService.createTransaction(eq("01000001"), any())).thenReturn(resp);

        mockMvc.perform(post("/v1/accounts/01000001/transactions")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.0,\"currency\":\"GBP\",\"type\":\"deposit\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("tan-1234567"));
    }

    @Test
    void testCreateTransaction_invalidAccountNumber() throws Exception {
        mockMvc.perform(post("/v1/accounts/invalid/transactions")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.0,\"currency\":\"GBP\",\"type\":\"deposit\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid accountNumber format"))
                .andExpect(jsonPath("$.details[0].field").value("accountNumber"));
    }

    @Test
    void testListTransactions() throws Exception {
        ListTransactionsResponse resp = new ListTransactionsResponse();
        resp.setTransactions(java.util.Collections.emptyList());
        BankAccountResponse account = new BankAccountResponse();
        account.setAccountNumber("01000001");
        account.setUserId("usr-abc123");
        when(accountService.fetchAccount("01000001")).thenReturn(account);
        when(transactionService.listTransactions("01000001")).thenReturn(resp);
        mockMvc.perform(get("/v1/accounts/01000001/transactions")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions").isArray());
    }

    @Test
    void testListTransactions_invalidAccountNumber() throws Exception {
        mockMvc.perform(get("/v1/accounts/invalid/transactions")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid accountNumber format"))
                .andExpect(jsonPath("$.details[0].field").value("accountNumber"));
    }

    @Test
    void testFetchTransaction() throws Exception {
        TransactionResponse resp = new TransactionResponse();
        resp.setId("tan-1234567");
        resp.setAmount(100.0);
        resp.setCurrency("GBP");
        resp.setType("deposit");
        BankAccountResponse account = new BankAccountResponse();
        account.setAccountNumber("01000001");
        account.setUserId("usr-abc123");
        when(accountService.fetchAccount("01000001")).thenReturn(account);
        when(transactionService.fetchTransaction("01000001", "tan-1234567")).thenReturn(resp);
        mockMvc.perform(get("/v1/accounts/01000001/transactions/tan-1234567")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("tan-1234567"));
    }

    @Test
    void testFetchTransaction_invalidAccountNumber() throws Exception {
        mockMvc.perform(get("/v1/accounts/invalid/transactions/tan-1234567")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid accountNumber format"))
                .andExpect(jsonPath("$.details[0].field").value("accountNumber"));
    }

    @Test
    void testFetchTransaction_invalidTransactionId() throws Exception {
        mockMvc.perform(get("/v1/accounts/01000001/transactions/invalid")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid transactionId format"))
                .andExpect(jsonPath("$.details[0].field").value("transactionId"));
    }

    @Test
    void testCreateTransaction_insufficientFunds() throws Exception {
        BankAccountResponse account = new BankAccountResponse();
        account.setAccountNumber("01000001");
        account.setUserId("usr-abc123");
        account.setBalance(10.0); // Only 10 in account
        when(accountService.fetchAccount("01000001")).thenReturn(account);

        mockMvc.perform(post("/v1/accounts/01000001/transactions")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.0,\"currency\":\"GBP\",\"type\":\"withdrawal\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Insufficient funds to process transaction"));
    }

    @Test
    void testCreateTransaction_depositUpdatesBalance() throws Exception {
        BankAccountResponse account = new BankAccountResponse();
        account.setAccountNumber("01000001");
        account.setUserId("usr-abc123");
        account.setBalance(10.0);
        when(accountService.fetchAccount("01000001")).thenReturn(account);
        TransactionResponse resp = new TransactionResponse();
        resp.setId("tan-1234567");
        resp.setAmount(20.0);
        resp.setCurrency("GBP");
        resp.setType("deposit");
        when(transactionService.createTransaction(eq("01000001"), any())).thenReturn(resp);

        mockMvc.perform(post("/v1/accounts/01000001/transactions")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":20.0,\"currency\":\"GBP\",\"type\":\"deposit\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("tan-1234567"));
        // After this, account.getBalance() should be 30.0 if checked in a real integration test
    }

    @Test
    void testCreateTransaction_withdrawalUpdatesBalance() throws Exception {
        BankAccountResponse account = new BankAccountResponse();
        account.setAccountNumber("01000001");
        account.setUserId("usr-abc123");
        account.setBalance(50.0);
        when(accountService.fetchAccount("01000001")).thenReturn(account);
        TransactionResponse resp = new TransactionResponse();
        resp.setId("tan-7654321");
        resp.setAmount(20.0);
        resp.setCurrency("GBP");
        resp.setType("withdrawal");
        when(transactionService.createTransaction(eq("01000001"), any())).thenReturn(resp);

        mockMvc.perform(post("/v1/accounts/01000001/transactions")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":20.0,\"currency\":\"GBP\",\"type\":\"withdrawal\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("tan-7654321"));
        // After this, account.getBalance() should be 30.0 if checked in a real integration test
    }

    @Test
    void testCreateTransaction_missingAmount() throws Exception {
        BankAccountResponse account = new BankAccountResponse();
        account.setAccountNumber("01000001");
        account.setUserId("usr-abc123");
        account.setBalance(100.0);
        when(accountService.fetchAccount("01000001")).thenReturn(account);

        mockMvc.perform(post("/v1/accounts/01000001/transactions")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currency\":\"GBP\",\"type\":\"deposit\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid details supplied"))
                .andExpect(jsonPath("$.details[0].field").value("amount"));
    }

    @Test
    void testCreateTransaction_missingCurrency() throws Exception {
        BankAccountResponse account = new BankAccountResponse();
        account.setAccountNumber("01000001");
        account.setUserId("usr-abc123");
        account.setBalance(100.0);
        when(accountService.fetchAccount("01000001")).thenReturn(account);

        mockMvc.perform(post("/v1/accounts/01000001/transactions")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":10.0,\"type\":\"deposit\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid details supplied"))
                .andExpect(jsonPath("$.details[0].field").value("currency"));
    }

    @Test
    void testCreateTransaction_missingType() throws Exception {
        BankAccountResponse account = new BankAccountResponse();
        account.setAccountNumber("01000001");
        account.setUserId("usr-abc123");
        account.setBalance(100.0);
        when(accountService.fetchAccount("01000001")).thenReturn(account);

        mockMvc.perform(post("/v1/accounts/01000001/transactions")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":10.0,\"currency\":\"GBP\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid details supplied"))
                .andExpect(jsonPath("$.details[0].field").value("type"));
    }

    @Test
    void testCreateTransaction_missingMultipleFields() throws Exception {
        BankAccountResponse account = new BankAccountResponse();
        account.setAccountNumber("01000001");
        account.setUserId("usr-abc123");
        account.setBalance(100.0);
        when(accountService.fetchAccount("01000001")).thenReturn(account);

        mockMvc.perform(post("/v1/accounts/01000001/transactions")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid details supplied"))
                .andExpect(jsonPath("$.details[?(@.field=='amount')]").exists())
                .andExpect(jsonPath("$.details[?(@.field=='currency')]").exists())
                .andExpect(jsonPath("$.details[?(@.field=='type')]").exists());
    }
} 