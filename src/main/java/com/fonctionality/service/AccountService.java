package com.fonctionality.service;

import com.fonctionality.dto.response.SoldWithDate;
import com.fonctionality.entity.Account;
import com.fonctionality.entity.Transaction;
import com.fonctionality.entity.TransferHistory;
import com.fonctionality.entity.TypeAccount;
import com.fonctionality.repository.AccountRepository;
import com.fonctionality.repository.TransferHistoryRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;
    private final TransferHistoryRepository transferHistoryRepository;

    public AccountService(AccountRepository accountRepository, TransferHistoryRepository transferHistoryRepository){
        this.accountRepository = accountRepository;
        this.transferHistoryRepository = transferHistoryRepository;
    }

    public List<Account> carryOfTransfer(Account sender, Account recipient, double amount) {
        if (sender.equals ( recipient)) {
            if (sender.getAccount_type ().equals ( TypeAccount.BANK )) {
                return getAccounts(sender, recipient, amount);
            } else {
                if (sender.getBalance () - amount >= 0) {
                    return getAccounts(sender, recipient, amount);
                }
                throw new RuntimeException ("Impossible to transfer because your balance is insufficient");
            }
        }else {
            throw new RuntimeException ( "Cannot transfer money to the same account." );
        }
    }

    private List<Account> getAccounts(Account sender, Account recipient, double amount) {
        sender.credit ( recipient, amount );
        Account account = this.accountRepository.update ( sender );
        Account account1 = this.accountRepository.update ( recipient );
        TransferHistory transferHistory = TransferHistory.builder()
                .transferDate(LocalDateTime.now())
                .creditTransaction(sender)
                .debitTransaction(recipient)
                .build();
        this.transferHistoryRepository.save(transferHistory);
        return List.of (account1, account  );
    }


}
