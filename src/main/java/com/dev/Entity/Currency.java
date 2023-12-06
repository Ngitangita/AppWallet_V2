package com.dev.Entity;

import java.util.Objects;

public class Currency {
    private Long id;
    private NameCurrency name;
    private CodeCurrency code;

    public Currency(Long id, NameCurrency name, CodeCurrency code){
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public NameCurrency getName(){
        return name;
    }

    public void setName(NameCurrency name){
        this.name = name;
    }

    public CodeCurrency getCode(){
        return code;
    }

    public void setCode(CodeCurrency code){
        this.code = code;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass ( ) != o.getClass ( )) return false;
        Currency currency = (Currency) o;
        return Objects.equals ( id, currency.id ) && name == currency.name && code == currency.code;
    }

    @Override
    public int hashCode(){
        return Objects.hash ( id, name, code );
    }

    @Override
    public String toString(){
        return "Currency{" +
                "id=" + id +
                ", name=" + name +
                ", code=" + code +
                '}';
    }
}
