public class DiningPhilosophersProblem {

    public static void main(String[] args) {
        int n = 5;

        // create and initialize all forks
        Object forks[] = new Object[n];
        for (int i = 0; i < n; i++)
            forks[i] = new Object();

        // create the philosophers and start them as threads
        for (int i = 0; i < n; i++) {
            Object leftFork = forks[i];
            Object rightFork = forks[(i + 1) % n];
            Philosopher philosopher;
            philosopher = new Philosopher(leftFork, rightFork);
            Thread thread = new Thread(philosopher);
            thread.setName("p" + i);
            thread.start();
        }
    }
}