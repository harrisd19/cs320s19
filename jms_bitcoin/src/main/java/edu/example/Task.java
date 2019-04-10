package edu.example;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class Task implements Serializable {

    private byte data[];
    private byte leadingZeros; // [1-4]
    private byte nonce[];      // always 32 bits (4 bytes)
    private Random rnd;
    private byte digest[];

    private static final byte MIN_LEADING_ZEROS = 1;
    private static final byte MAX_LEADING_ZEROS = 4;
    private static final byte DEFAULT_DATA_SIZE = 32;
    public static final byte  NONCE_SIZE = 4;
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final int  DIGEST_LENGTH = 16; // in bytes

    Task(byte data[], byte leadingZeros) throws NoSuchAlgorithmException {
        this.data = data;
        if (leadingZeros < MIN_LEADING_ZEROS)
            this.leadingZeros = MIN_LEADING_ZEROS;
        else if (leadingZeros > MAX_LEADING_ZEROS)
            this.leadingZeros = MAX_LEADING_ZEROS;
        else
            this.leadingZeros = leadingZeros;
        nonce = new byte[NONCE_SIZE];
        rnd = new Random();
        digest = null;
    }

    Task() throws NoSuchAlgorithmException {
        data = new byte[DEFAULT_DATA_SIZE];
        for (int i = 0; i < data.length; i++)
            data[i] = (byte) ALPHABET.charAt(rnd.nextInt(ALPHABET.length()));
        leadingZeros = MIN_LEADING_ZEROS;
        nonce = new byte[NONCE_SIZE];
        rnd = new Random();
        digest = null;
    }

    byte[] getData() {
        return data;
    }

    byte getLeadingZeros() {
        return leadingZeros;
    }

    byte[] getNonce() {
        return nonce;
    }

    byte[] getDigest() {
        return digest;
    }

    void generateNonce() {
        rnd.nextBytes(nonce);
        computeHash();
    }

    private void computeHash() {
        MessageDigest hashFunction = null;
        try {
            hashFunction = MessageDigest.getInstance("MD5");
            byte allData[] = new byte[data.length + nonce.length];
            for (int i = 0; i < data.length; i++)
                allData[i] = data[i];
            for (int i = 0; i < nonce.length; i++)
                allData[i + data.length] = nonce[i];
            digest = hashFunction.digest(allData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    boolean isSolved() {
        if (digest == null || digest.length != DIGEST_LENGTH)
            return false;
        boolean solved = true;
        for (int i = 0; i < leadingZeros; i++)
            if (digest[i] != 0) {
                solved = false;
                break;
            }
        return solved;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < data.length; i++)
            str += data[i] + " ";
        str = str.substring(0, str.length() - 1);
        str += "," + leadingZeros + ",";
        for (int i = 0; i < nonce.length; i++)
            str += nonce[i] + " ";
        str = str.substring(0, str.length() - 1);
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        Task other = (Task) obj;
        return Arrays.equals(data, other.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Task clone = null;
        try {
            clone = new Task(data, leadingZeros);
        } catch (NoSuchAlgorithmException e) {
        }
        return clone;
    }

    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException {
//        // use this to generate the input.txt file
//        PrintStream out = new PrintStream(new FileOutputStream("input.txt"));
//        for (int i = 0; i < 10; i++) {
//            Task task = new Task();
//            out.println(task);
//        }
//        out.close();

        byte data1[] = {1, 2, 3, 4};
        byte data2[] = {1, 2, 3, 4};

        Task task1 = new Task(data1, (byte) 1);
        Task task2 = new Task(data2, (byte) 1);

        System.out.println(task1.hashCode());
        System.out.println(task2.hashCode());

    }
}
