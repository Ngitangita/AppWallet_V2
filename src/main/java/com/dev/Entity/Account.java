package com.dev.Entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Account {
    private Long id;
    private AccountName name;
    private Double balance;
    private LocalDateTime lastUpdateDateTime;
    private List<Transaction> transactions;
    private Currency currency;
    private TypeAccount account_type;

    public Account(Long id, AccountName name, Double balance, LocalDateTime lastUpdateDateTime, List<Transaction> transactions, Currency currency, TypeAccount account_type){
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.transactions = transactions;
        this.currency = currency;
        this.account_type = account_type;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public AccountName getName(){
        return name;
    }

    public void setName(AccountName name){
        this.name = name;
    }

    public Double getBalance(){
        return balance;
    }

    public void setBalance(Double balance){
        this.balance = balance;
    }

    public LocalDateTime getLastUpdateDateTime(){
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(LocalDateTime lastUpdateDateTime){
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions){
        this.transactions = transactions;
    }

    public Currency getCurrency(){
        return currency;
    }

    public void setCurrency(Currency currency){
        this.currency = currency;
    }

    public TypeAccount getAccount_type(){
        return account_type;
    }

    public void setAccount_type(TypeAccount account_type){
        this.account_type = account_type;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass ( ) != o.getClass ( )) return false;
        Account account = (Account) o;
        return Objects.equals ( id, account.id ) && name == account.name && Objects.equals ( balance, account.balance ) && Objects.equals ( lastUpdateDateTime, account.lastUpdateDateTime ) && Objects.equals ( transactions, account.transactions ) && Objects.equals ( currency, account.currency ) && account_type == account.account_type;
    }

    @Override
    public int hashCode(){
        return Objects.hash ( id, name, balance, lastUpdateDateTime, transactions, currency, account_type );
    }

    @Override
    public String toString(){
        return "Account{" +
                "id=" + id +
                ", name=" + name +
                ", balance=" + balance +
                ", lastUpdateDateTime=" + lastUpdateDateTime +
                ", transactions=" + transactions +
                ", currency=" + currency +
                ", account_type=" + account_type +
                '}';
    }
}
