/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: Hwk 05 - VotingClient Class
 * Your name(s):
 */

import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;

public class VotingClient {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(VotingServer.RMI_REGISTRY_HOST);
        Voting stub = (Voting) registry.lookup("voting");

        // TODO: read voter's name
        Scanner sc = new Scanner(System.in);
        System.out.println("Voter's name? ");
        String voter = sc.nextLine();

        // TODO: if voting has already started, just quit the program
        if (stub.getPhase() != VotingServer.ANNOUNCED_PHASE)
            return;

        // TODO: display the ballot
        Ballot ballot = stub.getBallot();
        System.out.println(ballot);

        // TODO: wait for voting to open (sleep every 1s while waiting)
        System.out.println("Waiting for voting to open...");
        while (stub.getPhase() == VotingServer.ANNOUNCED_PHASE) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException ex) {

            }
        }

        // TODO: ask user for choice and cast vote
        System.out.println("Enter your vote now: ");
        String choice = sc.nextLine();
        if (!stub.castVote(voter, choice))
            System.out.println(voter + ", you\'ve already voted!");

        // TODO: wait for voting to close (sleep every 1s while waiting)
        System.out.println("Waiting for voting to close");
        while (stub.getPhase() == VotingServer.OPEN_PHASE) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException ex) {

            }
        }

        // TODO: display voting results
        System.out.println("Voting is closed now!");
        Map<String, Integer> results = stub.getResults();
        System.out.println("Election results: ");
        System.out.println(results);
    }
}
