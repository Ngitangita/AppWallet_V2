package com.fonctionality;

import com.fonctionality.repository.CurrencyRepository;

public class Main {
    public static void main(String[] args){
        CurrencyRepository cr = new CurrencyRepository();

        System.out.println(cr.findAll());
        System.out.println(cr.findById(1L));
    }


}