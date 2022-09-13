package com.mindhub.homebanking.utils;

public final class CardUtils {


    public static String getAccountNumber(){return "VIN-" + getRandomNumber(10000000,99999999);}

    public static int getCvv() {
        return getRandomNumber(100, 999);
    }

    public static String getCardNumber() {
        return getRandomNumber(1000,9999) + "-" + getRandomNumber(1000,9999) + "-" + getRandomNumber(1000,9999) + "-" + getRandomNumber(1000,9999);
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
