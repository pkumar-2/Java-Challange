package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountException;
import com.db.awmd.challenge.exception.BalanceTransferException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }



  private final Object lock = new Object();

  /**
   *
   * @param accId
   * @param acc
   * @param bal
   */
  @Override
  public void withdraw(String accId, Account acc, BigDecimal bal) {
    synchronized (lock) {
        accounts.get(accId).setBalance(acc.getBalance().subtract(bal));

    }
  }

  /**
   *
   * @param accId
   * @param acc
   * @param bal
   */
  @Override
  public void deposit(String accId, Account acc, BigDecimal bal) {
    synchronized (lock) {
        accounts.get(accId).setBalance(acc.getBalance().add(bal));
    }
  }


  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }

}
