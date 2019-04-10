/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: Hwk 05 - Voting Interface
 * Your name(s):
 */

import java.rmi.*;
import java.util.*;

public interface Voting extends Remote {

    Ballot getBallot() throws RemoteException;

    int getPhase() throws RemoteException;

    boolean castVote(final String voter, final String choice) throws RemoteException;

    Map<String, Integer> getResults() throws RemoteException;
}
