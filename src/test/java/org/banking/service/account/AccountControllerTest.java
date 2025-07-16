package org.banking.service.account;

import org.banking.service.controller.AccountController;
import org.banking.service.model.*;
import org.banking.service.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.banking.service.util.JwtUtil;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private String getAuthHeader() {
        return "Bearer " + JwtUtil.generateToken("usr-abc123");
    }

    @Test
    void testCreateAccount() throws Exception {
        CreateBankAccountRequest req = new CreateBankAccountRequest();
        req.setName("Test Account");
        req.setAccountType("personal");
        BankAccountResponse resp = new BankAccountResponse();
        resp.setAccountNumber("01000001");
        resp.setSortCode("10-10-10");
        resp.setName("Test Account");
        resp.setAccountType("personal");
        resp.setBalance(0.0);
        resp.setCurrency("GBP");
        when(accountService.createAccount(anyString(), any(CreateBankAccountRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Account\",\"accountType\":\"personal\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("01000001"));
    }

    @Test
    void testCreateAccount_missingName() throws Exception {
        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountType\":\"personal\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing required field: name"))
                .andExpect(jsonPath("$.details[0].field").value("name"));
    }

    @Test
    void testCreateAccount_missingAccountType() throws Exception {
        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Account\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing required field: accountType"))
                .andExpect(jsonPath("$.details[0].field").value("accountType"));
    }

    @Test
    void testCreateAccount_missingBothFields() throws Exception {
        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid details supplied"))
                .andExpect(jsonPath("$.details[?(@.field=='name')].message").value("Missing required field: name"))
                .andExpect(jsonPath("$.details[?(@.field=='accountType')].message").value("Missing required field: accountType"));
    }

    @Test
    void testCreateAccount_invalidAccountType() throws Exception {
        mockMvc.perform(post("/v1/accounts")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Account\",\"accountType\":\"business\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0].field").value("accountType"))
                .andExpect(jsonPath("$.details[0].message").value("Invalid accountType: only 'personal' is allowed"));
    }

    @Test
    void testListAccounts() throws Exception {
        ListBankAccountsResponse resp = new ListBankAccountsResponse();
        resp.setAccounts(java.util.Collections.emptyList());
        when(accountService.listAccounts(anyString())).thenReturn(resp);
        mockMvc.perform(get("/v1/accounts")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts").isArray());
    }

    @Test
    void testFetchAccount() throws Exception {
        BankAccountResponse resp = new BankAccountResponse();
        resp.setAccountNumber("01000001");
        resp.setSortCode("10-10-10");
        resp.setName("Test Account");
        resp.setAccountType("personal");
        resp.setBalance(0.0);
        resp.setCurrency("GBP");
        resp.setUserId("usr-abc123"); // Ensure account is owned by JWT subject
        when(accountService.fetchAccount("01000001")).thenReturn(resp);
        mockMvc.perform(get("/v1/accounts/01000001")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("01000001"));
    }

    @Test
    void testFetchAccount_invalidAccountNumber() throws Exception {
        mockMvc.perform(get("/v1/accounts/invalid")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid accountNumber format"))
                .andExpect(jsonPath("$.details[0].field").value("accountNumber"));
    }

    @Test
    void testUpdateAccount() throws Exception {
        UpdateBankAccountRequest req = new UpdateBankAccountRequest();
        req.setName("Updated");
        BankAccountResponse resp = new BankAccountResponse();
        resp.setAccountNumber("01000001");
        resp.setName("Updated");
        when(accountService.updateAccount(eq("01000001"), any())).thenReturn(resp);
        mockMvc.perform(patch("/v1/accounts/01000001")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void testUpdateAccount_invalidAccountType() throws Exception {
        mockMvc.perform(patch("/v1/accounts/01000001")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountType\":\"business\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid accountType: only 'personal' is allowed"));
    }

    @Test
    void testUpdateAccount_invalidAccountNumber() throws Exception {
        mockMvc.perform(patch("/v1/accounts/invalid")
                .header("Authorization", getAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid accountNumber format"))
                .andExpect(jsonPath("$.details[0].field").value("accountNumber"));
    }

    @Test
    void testDeleteAccount() throws Exception {
        BankAccountResponse resp = new BankAccountResponse();
        resp.setAccountNumber("01000001");
        resp.setUserId("usr-abc123");
        when(accountService.fetchAccount("01000001")).thenReturn(resp);
        doNothing().when(accountService).deleteAccount("01000001");
        mockMvc.perform(delete("/v1/accounts/01000001")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAccount_forbidden() throws Exception {
        BankAccountResponse resp = new BankAccountResponse();
        resp.setAccountNumber("01000001");
        resp.setUserId("usr-otheruser");
        when(accountService.fetchAccount("01000001")).thenReturn(resp);
        mockMvc.perform(delete("/v1/accounts/01000001")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Forbidden: You can only access your own bank account"));
    }

    @Test
    void testDeleteAccount_invalidAccountNumber() throws Exception {
        mockMvc.perform(delete("/v1/accounts/invalid")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid accountNumber format"))
                .andExpect(jsonPath("$.details[0].field").value("accountNumber"));
    }

    @Test
    void testDeleteAccount_notFound() throws Exception {
        when(accountService.fetchAccount("01000002")).thenReturn(null);
        mockMvc.perform(delete("/v1/accounts/01000002")
                .header("Authorization", getAuthHeader()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Account not found"));
    }
} 