package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountException;
import com.db.awmd.challenge.exception.BalanceTransferException;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }



  /**
   *
   * @param fromAccId
   * @param toAccId
   * @param value
   */
  public void transfer(String fromAccId,  String toAccId, BigDecimal value) {

    final Account sourceAcct;
    final Account destAcct;

    try {
      sourceAcct = accountsRepository.getAccount(fromAccId);
      log.info("source account info is" + sourceAcct.getAccountId() + " balance is " + sourceAcct.getBalance());//fixme if we get time it will get replaced with logger
    } catch (Exception exp) {
      throw new AccountException(String.valueOf("User account id " + fromAccId + ", does not exist!"));
    }

    try {
      destAcct = accountsRepository.getAccount(toAccId);
      log.info("destination account info is " + destAcct.getAccountId() + " balance is " + destAcct.getBalance());//fixme if we get time it will get replaced with logger
    } catch (Exception exp) {
      throw new AccountException(String.valueOf("User account id " + toAccId + ", does not exist!"));
    }

    if (sourceAcct.getBalance().compareTo(BigDecimal.ZERO) > 0 && sourceAcct.getBalance().compareTo(value) > 0) {
      accountsRepository.withdraw(fromAccId, sourceAcct, value);
      accountsRepository.deposit(toAccId, destAcct, value);
    } else {
      final String msg = String.valueOf(fromAccId + " your account does not have sufficient balance to transfer");
      log.info(msg);//fixme replace it with logger
      throw new BalanceTransferException(msg);
    }

  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }
}
