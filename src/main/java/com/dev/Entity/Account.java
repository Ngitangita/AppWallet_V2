package com.dev.Entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Account {
    private Long id;
    private AccountName name;
    private Double sold;
    private LocalDateTime updateAt;
    private List<Transaction> transactions;
    private Currency devise;
    private TypeAccount type;

    public Account(Long id, AccountName name, Double sold, LocalDateTime updateAt, List<Transaction> transactions, Currency devise, TypeAccount type){
        this.id = id;
        this.name = name;
        this.sold = sold;
        this.updateAt = updateAt;
        this.transactions = transactions;
        this.devise = devise;
        this.type = type;
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

    public Double getSold(){
        return sold;
    }

    public void setSold(Double sold){
        this.sold = sold;
    }

    public LocalDateTime getUpdateAt(){
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt){
        this.updateAt = updateAt;
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions){
        this.transactions = transactions;
    }

    public Currency getDevise(){
        return devise;
    }

    public void setDevise(Currency devise){
        this.devise = devise;
    }

    public TypeAccount getType(){
        return type;
    }

    public void setType(TypeAccount type){
        this.type = type;
    }

    public void effectueTransaction(Transaction transaction){
        transactions.add(transaction);
        sold += transaction.getAmount ();
    }


    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass ( ) != o.getClass ( )) return false;
        Account account = (Account) o;
        return Objects.equals ( id, account.id ) && name == account.name && Objects.equals ( sold, account.sold ) && Objects.equals ( updateAt, account.updateAt ) && Objects.equals ( transactions, account.transactions ) && Objects.equals ( devise, account.devise ) && type == account.type;
    }

    @Override
    public int hashCode(){
        return Objects.hash ( id, name, sold, updateAt, transactions, devise, type );
    }

    @Override
    public String toString(){
        return "Account{" +
                "id=" + id +
                ", name=" + name +
                ", sold=" + sold +
                ", updateAt=" + updateAt +
                ", transactions=" + transactions +
                ", devise=" + devise +
                ", type=" + type +
                '}';
    }
}
