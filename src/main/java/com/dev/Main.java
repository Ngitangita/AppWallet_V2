package com.dev;

import com.dev.Entity.Account;
import com.dev.Entity.SoldHistoryEntry;

public class Main {
    public static void main(String[] args){

        System.out.println (  System.getenv ( "PG_URL" ) );
        System.out.println (  System.getenv ( "PG_USER" ) );
        System.out.println (  System.getenv ( "PG_PASSWORD" ) );
    }




}