package com.takatsuka.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class MathOps {

    public boolean isPrime(int num){
        if(num % 2 == 0)
            return false;
        for(int i = 3; i <= Math.sqrt(num); i += 2){
            if(num % i == 0)
                return false;
        }
        return true;
    }

    public Hashtable<Integer, Integer> primeFactors(int num){
        Hashtable<Integer, Integer> factors = new Hashtable<>();

        while(num > 1){
            int spf = smallestPrimeFactor(num);

            factors.put(spf, factors.getOrDefault(spf, 0) + 1);

            num /= spf;
        }

        return factors;
    }

    private int smallestPrimeFactor(int num){
        // TODO improve to loglogn using the Sieve of Erdos
        if(num % 2 == 0)
            return 2;

        for(int i = 3; i <= Math.sqrt(num); i += 2){
            if(num % i == 0)
                return i;
        }

        return num;
    }

    public int totient(Hashtable<Integer, Integer> factors){
        int totientAcc = 1;
        for(Integer factor: factors.keySet()){
            int exp = factors.get(factor);
            totientAcc *= Math.pow(factor, exp) - Math.pow(factor, exp - 1);
        }
        return totientAcc;
    }

    public int totient(int num){
        return totient(primeFactors(num));
    }
}
