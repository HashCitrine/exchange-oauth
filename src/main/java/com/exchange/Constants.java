package com.exchange;

public class Constants {
    public enum ROLE {
        ADMIN,
        NORMAL
    }

    public enum YN {
        Y,
        N
    }

    public enum ORDER_TYPE{
        BUY,
        SELL
    }

    public enum TRANSACTION_TYPE{
        DEPOSIT,    //
        WITHDRAW    //
    }

    public enum STATUS {
        REQS,   //
        REQF,   //
        TCKS,   //
        TCKF,   //
        SUCC,   //
        FAIL    //
    }

    public enum TOPIC {
        submitDw,
        submitOrder
    }
}