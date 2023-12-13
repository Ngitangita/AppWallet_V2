package com.dev.Entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private Long id;
    private String label;
    private Double amount;
    private LocalDateTime dateTime;
    private TypeTransaction transactionType;
    private Account account;

    public Transaction(Long id, String label, Double amount, LocalDateTime dateTime, TypeTransaction transactionType, Account account){
        this.id = id;
        this.label = label;
        this.amount = amount;
        this.dateTime = dateTime;
        this.transactionType = transactionType;
        this.account = account;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getLabel(){
        return label;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public Double getAmount(){
        return amount;
    }

    public void setAmount(Double amount){
        this.amount = amount;
    }

    public LocalDateTime getDateTime(){
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime){
        this.dateTime = dateTime;
    }

    public TypeTransaction getTransactionType(){
        return transactionType;
    }

    public void setTransactionType(TypeTransaction transactionType){
        this.transactionType = transactionType;
    }

    public Account getAccount(){
        return account;
    }

    public void setAccount(Account account){
        this.account = account;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass ( ) != o.getClass ( )) return false;
        Transaction that = (Transaction) o;
        return Objects.equals ( id, that.id ) && Objects.equals ( label, that.label ) && Objects.equals ( amount, that.amount ) && Objects.equals ( dateTime, that.dateTime ) && transactionType == that.transactionType && Objects.equals ( account, that.account );
    }

    @Override
    public int hashCode(){
        return Objects.hash ( id, label, amount, dateTime, transactionType, account );
    }

    @Override
    public String toString(){
        return "Transaction{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                ", transactionType=" + transactionType +
                ", account=" + account +
                '}';
    }
}
