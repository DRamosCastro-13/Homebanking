package com.mindhub.homebanking.utils;


public class Utils {


    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String generateAccountNumber(){
        String number = "VIN-" + getRandomNumber(100000, 99999999);
        return number;
    }
}
