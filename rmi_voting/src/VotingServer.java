/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: Hwk 05 - VotingServer Class
 * Your name(s):
 */

import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;

public class VotingServer implements Voting {

    static final String BALLOT_FILE_NAME  = "ballot.txt";
    static final int    ANNOUNCED_PHASE   = 0;
    static final int    OPEN_PHASE        = 1;
    static final int    CLOSED_PHASE      = 2;
    static final String RMI_REGISTRY_HOST = "localhost";

    private Ballot       ballot;
    private int          phase;
    private List<String> voters;

    public VotingServer() throws FileNotFoundException {
        Scanner sc = new Scanner(new FileInputStream(BALLOT_FILE_NAME));
        String description = sc.nextLine();
        ballot = new Ballot(description);
        while (sc.hasNextLine()) {
            String option = sc.nextLine();
            ballot.add(option);
        }
        sc.close();
        phase  = ANNOUNCED_PHASE;
        voters = new LinkedList<>();
    }

    @Override
    public synchronized Ballot getBallot() throws RemoteException {
        if (phase == ANNOUNCED_PHASE)
            return ballot;
        return null;
    }

    @Override
    public synchronized int getPhase() throws RemoteException {
        return phase;
    }

    @Override
    public synchronized boolean castVote(final String voter, final String choice) throws RemoteException {
        if (phase == OPEN_PHASE && !voters.contains(voter)) {
            voters.add(voter);
            return ballot.castVote(choice);
        }
        return false;
    }

    @Override
    public synchronized Map<String, Integer> getResults() {
        if (phase == CLOSED_PHASE)
            return ballot.getChoices();
        return null;
    }

    public synchronized void setPhase(int phase) {
        if (phase == ANNOUNCED_PHASE || phase == OPEN_PHASE || phase == CLOSED_PHASE)
            this.phase = phase;
    }

    void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println(ballot);
        System.out.println("Server Ready!");
        System.out.println("Hit [ENTER] for start accepting votes...");
        sc.nextLine();

        setPhase(OPEN_PHASE);
        System.out.println("Hit [ENTER] for close voting...");
        sc.nextLine();

        setPhase(CLOSED_PHASE);
        System.out.println("Results:");
        System.out.println(ballot.getChoices());
    }

    public static void main(String[] args) throws FileNotFoundException, RemoteException {
        VotingServer votingServer = new VotingServer();
        Voting skeleton = (Voting) UnicastRemoteObject.exportObject(votingServer, 0);
        Registry registry = LocateRegistry.getRegistry(RMI_REGISTRY_HOST);
        registry.rebind("voting", skeleton);
        votingServer.run();
    }
}
