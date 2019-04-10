/*
 * CSCI121 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: Hwk 03 - Account
 * Your name(s):
 */

 public class Account {

     private int balance;

     Account(int balance) {
         this.balance = balance;
     }

     Account() {
         this(0);
     }

     // take the amount from balance and sleep for 500ms
     void withdraw(int amount) {
         this.balance -= amount;
         try {
             Thread.sleep(500);
         }
         catch (InterruptedException ex) {

         }
     }

     // add the amount to balance
     void deposit(int amount) {
         this.balance += amount;
     }

     // TODO: make changes so that the transfer does not cause deadlock
     void transfer(Account to, int amount) {
         String threadName = Thread.currentThread().getName();
         synchronized (this) {
             System.out.println("Thread " + threadName + " is trying to withdraw...");
             this.withdraw(amount);
             System.out.println("Thread " + threadName + " is done with the withdraw!");
             synchronized (to) {
                 System.out.println("Thread " + threadName + " is trying to deposit...");
                 to.deposit(amount);
                 System.out.println("Thread " + threadName + " is done with deposit!");
             }
         }
     }

     void transferNoHanging(Account to, int amount) {
         String threadName = Thread.currentThread().getName();
         synchronized (this) {
             System.out.println("Thread " + threadName + " is trying to withdraw...");
             this.withdraw(amount);
             System.out.println("Thread " + threadName + " is done with the withdraw!");
         }
         synchronized (to) {
             System.out.println("Thread " + threadName + " is trying to deposit...");
             to.deposit(amount);
             System.out.println("Thread " + threadName + " is done with deposit!");
         }
     }

     @Override
     public String toString() {
         return "balance=" + balance;
     }

     public static void main(String[] args) {
         Account accountA = new Account(100);
         Account accountB = new Account(50);
         System.out.println("accountA " + accountA);
         System.out.println("accountB " + accountB);

         int amount = 10;
         Thread t1 = new Thread(new Runnable() {
             @Override
             public void run() {
                 System.out.println("Thread t1: accountA -> accountB");
                 accountA.transferNoHanging(accountB, amount);
                 System.out.println("Thread t1 is done!");
             }
         }
         );
         t1.setName("t1");

         Thread t2 = new Thread(new Runnable() {
             @Override
             public void run() {
                 System.out.println("Thread t2: accountB -> accountA");
                 accountB.transferNoHanging(accountA, amount);
                 System.out.println("Thread t2 is done!");
             }
         }
         );
         t2.setName("t2");

         t1.start();
         t2.start();

         // TODO: have the main thread wait indefinitely for t1 and t2 to finish
         try {
             t1.join();
             t2.join();
         }
         catch (InterruptedException ex) {
         }

         System.out.println("accountA " + accountA);
         System.out.println("accountB " + accountB);
     }
 }
