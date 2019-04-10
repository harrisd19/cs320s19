import java.util.Scanner;

public class ThreadDemo {

    static final int RANGE = 100;
    static final int SLEEP = 50;

    public static void main(String[] args) {

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                for (int i = 0; i < RANGE; i++)
                    if (i % 2 == 0) {
                        System.out.println(name + ": " + i);
                        try {
                            Thread.sleep(SLEEP);
                        }
                        catch (InterruptedException ex) {

                        }
                    }
                System.out.println(name + " is done!");
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                for (int i = 0; i < RANGE; i++)
                    if (i % 2 != 0) {
                        System.out.println(name + ": " + i);
                        try {
                            Thread.sleep(SLEEP);
                        } catch (InterruptedException ex) {

                        }
                    }
                System.out.println(name + " is done!");
            }
        };

        Thread t1 = new Thread(r1);
        t1.setName("t1");

        Thread t2 = new Thread(r2);
        t2.setName("t2");

        t1.start();
        t2.start();

        (new Scanner(System.in)).nextLine();
    }
}
