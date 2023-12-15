package com.fonctionality.service;

import com.fonctionality.entity.Account;
import com.fonctionality.entity.TypeAccount;
import com.fonctionality.repository.AccountRepository;

import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> carryOfTransfer(Account sender, Account recipient, double amount) {
        if (sender.equals ( recipient)) {
            if (sender.getAccount_type ().equals ( TypeAccount.BANK )) {
               sender.credit ( recipient, amount );
               Account account = this.accountRepository.update ( sender );
               Account account1 = this.accountRepository.update ( recipient );
                return List.of (account1, account  );
            } else {
                if (sender.getBalance () - amount >= 0) {
                    sender.credit ( recipient, amount );
                    Account account = this.accountRepository.update ( sender );
                    Account account1 = this.accountRepository.update ( recipient );
                    return List.of (account1, account  );
                }
                throw new RuntimeException ("Impossible to transfer because your balance is insufficient");
            }
        }else {
            throw new RuntimeException ( "Cannot transfer money to the same account." );
        }
    }
}
