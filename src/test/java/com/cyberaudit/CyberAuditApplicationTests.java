package com.cyberaudit;

import com.cyberaudit.domain.UserAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CyberAuditApplicationTests {

    @Test
    void userAccountContainsNoPlaintextPassword() {
        UserAccount account = new UserAccount(1L, "Teste", "teste@example.com", "LEITOR", 2L, "Org", "2026-01-01");
        assertEquals("LEITOR", account.role());
        assertEquals("teste@example.com", account.email());
    }
}
