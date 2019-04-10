/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: Hwk 05 - Ballot Class
 * Your name(s):
 */

import java.io.*;
import java.util.*;

public class Ballot implements Serializable {

    private String               description;
    private Map<String, Integer> choices;

    Ballot(final String description) {
        this.description = description;
        choices = new HashMap<>();
    }

    void add(final String choice) {
        choices.put(choice, 0);
    }

    boolean castVote(final String choice) {
        if (choices.containsKey(choice)) {
            choices.put(choice, choices.get(choice) + 1);
            return true;
        }
        return false;
    }

    Map<String, Integer> getChoices() {
        return choices;
    }

    @Override
    public String toString() {
        return "Ballot{" +
                "description='" + description + '\'' +
                ", choices=" + choices +
                '}';
    }
}

