package com.dev.Entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class SoldHistoryEntry {
    private LocalDateTime dateTime;
    private double balance;

    public SoldHistoryEntry(LocalDateTime dateTime, double balance) {
        this.dateTime = dateTime;
        this.balance = balance;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoldHistoryEntry that = (SoldHistoryEntry) o;
        return Double.compare(balance, that.balance) == 0 && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, balance);
    }

    @Override
    public String toString() {
        return "SoldHistoryEntry{" +
                "dateTime=" + dateTime +
                ", balance=" + balance +
                '}';
    }
}
