package com.db.awmd.challenge;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.AccountsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAsync
@WebAppConfiguration
public class BalanceTransferTest {

    private MockMvc mockMvc;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void prepareMockMvc() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

        // Reset the existing accounts before each test.
        accountsService.getAccountsRepository().clearAccounts();
    }


   @Test
    public void balanceTransferTest() throws InterruptedException{
        createAccount();
        Thread.sleep(2000);
        for(int i=1;i<11;i++){
            for (int j=1;j<11;j++){
                if(i!=j)
                    transfer(String.valueOf(i),String.valueOf(j),2000);
            }
        }

    }


    private void createAccount(){
        Account account;
        for(int i=1;i<11;i++){
            account = new Account(String.valueOf(i),new BigDecimal("10000"));
            createAccount(account);
        }
    }

    @Async
    private void createAccount(Account account){
        accountsService.createAccount(account);
    }

    @Async
    private void transfer(String fromAcct,String toAcct,int maxAmount){
        accountsService.transfer(fromAcct, toAcct, new BigDecimal(maxAmount));
    }
}
