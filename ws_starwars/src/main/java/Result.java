/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: Hwk 04 - Result
 * Your name(s):
 */

import java.util.Arrays;

public class Result {

    int count;
    String next, previous;
    People results[];

    @Override
    public String toString() {
        return "Result{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", results=" + Arrays.toString(results) +
                '}';
    }
}
