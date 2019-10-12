package com.itcag.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public final class MathToolbox {
    
    public final static String getRandomUUID() {
        return getRandomUUID(32);
    }
    
    public final static String getRandomUUID(int size) {
        
        Random randomizer = new Random();

        StringBuilder sb = new StringBuilder();
        while(sb.length() < size) sb.append(Integer.toHexString(randomizer.nextInt()));

        return sb.toString().substring(0, size);

    }

    public final static int getRandomNumber(int min, int max) {
        Random r = new Random();
	return r.nextInt((max - min) + 1) + min;
    }
    
    public static double roundDouble(double value, int places) {
        
        if (places < 0) throw new IllegalArgumentException();

        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } catch (Exception ex) {
            return 0.00;
        }

    }

    public static double sigmoid(double x) {
        return (1/( 1 + Math.pow(Math.E,(-1*x))));
    }
    
}
