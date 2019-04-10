package edu.example;

import javax.jms.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class BitcoinMiningSimulation {

    public static final String FILE_NAME = "input.txt";
    private List<Task> tasks;
    private Random rnd;

    BitcoinMiningSimulation(String fileName) throws FileNotFoundException, NoSuchAlgorithmException {
        // read tasks from the input file
        tasks = new LinkedList<>();
        Scanner in = new Scanner(new FileInputStream(FILE_NAME));
        while (in.hasNextLine()) {
            String line[] = in.nextLine().split(",");
            String temp[] = line[0].split(" ");
            byte data[] = new byte[temp.length];
            for (int i = 0; i < temp.length; i++)
                data[i] = Byte.parseByte(temp[i]);
            byte leadingZeros = Byte.parseByte(line[1]);
            Task task = new Task(data, leadingZeros);
            tasks.add(task);
        }
        in.close();
        rnd = new Random();
    }

    byte[] solveTask(Task task) throws NoSuchAlgorithmException {
        while (true) {
            task.generateNonce();
            if (task.isSolved())
                return task.getDigest();
        }
    }

    void run() throws NoSuchAlgorithmException {
        for (Task task: tasks) {
            System.out.println("Task: " + task);
            byte digest[] = solveTask(task);
            System.out.println("Solution found! Resulting hash: " + Arrays.toString(digest));
            System.out.println();
        }
    }

    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException {
        BitcoinMiningSimulation bitcoinMining = new BitcoinMiningSimulation(BitcoinMiningSimulation.FILE_NAME);
        bitcoinMining.run();
    }
}
