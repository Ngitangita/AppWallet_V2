package com.dev.Entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private Long id;
    private String label;
    private Double amount;
    private LocalDateTime transactionDateTim;
    private TypeTransaction type;

    private Account account;


    public Transaction(Long id, String label, Double amount, LocalDateTime transactionDateTim, TypeTransaction type, Account account) {
        this.id = id;
        this.label = label;
        this.amount = amount;
        this.transactionDateTim = transactionDateTim;
        this.type = type;
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
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

    public LocalDateTime getTransactionDateTim(){
        return transactionDateTim;
    }

    public void setTransactionDateTim(LocalDateTime transactionDateTim){
        this.transactionDateTim = transactionDateTim;
    }

    public TypeTransaction getType(){
        return type;
    }

    public void setType(TypeTransaction type){
        this.type = type;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass ( ) != o.getClass ( )) return false;
        Transaction that = (Transaction) o;
        return Objects.equals ( id, that.id ) && Objects.equals ( label, that.label ) && Objects.equals ( amount, that.amount ) && Objects.equals ( transactionDateTim, that.transactionDateTim ) && type == that.type;
    }

    @Override
    public int hashCode(){
        return Objects.hash ( id, label, amount, transactionDateTim, type );
    }

    @Override
    public String toString(){
        return "Transaction{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", amount=" + amount +
                ", transactionDateTim=" + transactionDateTim +
                ", type=" + type +
                '}';
    }
}
