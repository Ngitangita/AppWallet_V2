package services;

import entitries.Account;
import entitries.Transaction;
import entitries.TypeAccount;
import entitries.TypeTransaction;
import exceptions.TransactionError;
import lombok.AllArgsConstructor;
import repositories.AccountRepository;
import repositories.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class TransactionServices {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public Account creditOrDebitSold(Long id, TypeTransaction type, double amount, String label) {
        final Account account = this.accountRepository.findById(id);
        final double oldSold = account.getBalance();

        if (type.equals(TypeTransaction.DEBIT)) {
            if (!account.getAccount_type ().equals(TypeAccount.BANK) && amount > oldSold) {
                throw new TransactionError("Insufficient balance for a debit transaction.");
            }
            account.setBalance(oldSold - amount);
            this.accountRepository.update(account);
        } else {
            account.setBalance(oldSold + amount);
            this.accountRepository.update(account);
        }

        Transaction newTransaction = Transaction.builder()
                .account(account)
                .typeTransaction(type)
                .dateTime(LocalDateTime.now())
                .label(label)
                .amount(amount)
                .build();

       this.transactionRepository.save ( newTransaction );

        List<Transaction> transactions = this.transactionRepository.findAll ().stream( ).filter ( t -> t.getAccount ().getId ().equals ( account.getId () ) ).toList ( );
        account.setTransactions ( transactions );
        return account;
    }
}
