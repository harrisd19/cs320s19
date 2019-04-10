package edu.example;

public class PasswordBreakerSingleProcess {

    private PasswordGenerator pwdGen;
    private static final int PASSWORD_SIZE = 5;

    PasswordBreakerSingleProcess(int size) {
        pwdGen = new PasswordGenerator(size);
        System.out.println("Password to break is: " + pwdGen.getPassword());
    }

    void search(final String password) {
        int size = password.length();

        // pruning
        if (size > pwdGen.getSize())
            return;

        // does the password match?
        if (size == pwdGen.getSize()) {
            if (pwdGen.match(password)) {
                System.out.println("Password cracked: " + password);
                return;
            }
        }

        // from here and on, the password is not big enough
        else
            for (int i = 0; i < PasswordGenerator.ALPHABET.length(); i++) {
                String tempPassword = password + PasswordGenerator.ALPHABET.charAt(i);
                search(tempPassword);
            }
    }

    void run() {
        search("");
    }

    public static void main(String[] args) {
        PasswordBreakerSingleProcess pwdBreaker = new PasswordBreakerSingleProcess(PASSWORD_SIZE);
        long startTime = System.nanoTime();
        pwdBreaker.run();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Running time " + duration / 1000000000. + "s");
    }
}
