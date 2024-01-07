package services;

import entitries.*;
import exceptions.AccountError;
import exceptions.TransactionError;
import lombok.AllArgsConstructor;
import repositories.AccountRepository;
import repositories.TransactionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TransactionServices {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Créer une fonction qui permet d’effectuer une transaction dans un compte. (débit ou crédit)
     */
    public Account creditOrDebitSold(Long id, TypeTransaction type, double amount, String label) {
        final Account account = this.accountRepository.findById(id);
        if (account != null){
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

            List<Transaction> transactions = this.transactionRepository.findAll ()
                    .stream( ).filter ( t -> (t.getAccount () != null) && t.getAccount ().getId ().equals ( account.getId () ) )
                    .toList ( );
            account.setTransactions ( transactions );
            return account;
        }
        throw new  AccountError ( "Account id "+ id + "not fount" );
    }

    /**
     *  une fonction qui permet d’obtenir le solde d’un compte à une date et heure donnée
     */
    
    public double getBalanceGiveDateTime(Long id, LocalDateTime dateTimeGive){
        Account account = this.accountRepository.findById ( id );
        if (account != null ){
            double  sold = 0.0d;
            List<Transaction> transactions = this.findAllByAccountId(id);
            List<Transaction> relevantTransactions = transactions.stream()
                    .filter(transaction -> transaction.getDateTime().isBefore(dateTimeGive) || transaction.getDateTime().isEqual(dateTimeGive))
                    .toList ( );
            for (Transaction transaction : relevantTransactions) {
                if (transaction.getTypeTransaction() == TypeTransaction.CREDIT) {
                    sold += transaction.getAmount();
                } else {
                    sold -= transaction.getAmount();
                }
            }

            return sold;
        }
        throw new  AccountError ( "Account id "+ id + "not fount" );
    }

    public List<Transaction> findAllByAccountId(Long id){
      return this.transactionRepository.findAll ()
              .stream( ).filter ( t ->( t.getAccount () != null) && t.getAccount ().getId ().equals ( id )  )
              .toList ();
    }


    /**
     * Créer une fonction qui permet d’obtenir l’historique du solde d’un compte dans une intervalle de date et heure donnée.
     */
    public List<BalanceHistory> getBalanceHistory(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Account account = accountRepository.findById(id);
      if (account != null){
          List<Transaction> transactions = this.findAllByAccountId(id);
          List<BalanceHistory> balanceHistory = new ArrayList<> ();
          double currentBalance = 0.0;

          List<Transaction> relevantTransactions = transactions.stream()
                  .filter(transaction -> transaction.getDateTime().isAfter(startDateTime) && transaction.getDateTime().isBefore(endDateTime))
                  .toList ();

          for (Transaction transaction : relevantTransactions) {
              if (transaction.getTypeTransaction() == TypeTransaction.CREDIT) {
                  currentBalance += transaction.getAmount();
              } else {
                  currentBalance -= transaction.getAmount();
              }

              balanceHistory.add(new BalanceHistory (transaction.getDateTime(), currentBalance));
          }

          return balanceHistory;
       }
        throw new  AccountError ( "Account id "+ id + "not fount" );
      }

}
