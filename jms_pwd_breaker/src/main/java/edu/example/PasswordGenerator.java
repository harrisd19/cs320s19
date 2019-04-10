package edu.example;

import java.util.Random;

public class PasswordGenerator {

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789#$%&*!";
    private String password;
    private int    size;
    private static final int MINIMUM_SIZE = 5;
    private Random rnd;

    PasswordGenerator(int size) {
        if (size < MINIMUM_SIZE)
            this.size = MINIMUM_SIZE;
        else
            this.size = size;
        rnd = new Random();
        generate();
    }

    void generate() {
        char temp[] = new char[size];
        for (int i = 0; i < size; i++)
            temp[i] = ALPHABET.charAt(rnd.nextInt(ALPHABET.length()));
        password = new String(temp);
    }

    String getPassword() {
        return password;
    }

    boolean match(final String password) {
        return this.password.equals(password);
    }

    int getSize() {
        return size;
    }
}
