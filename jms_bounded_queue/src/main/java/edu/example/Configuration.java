/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: Hwk 06 - Configuration Class
 * Your name(s):
 */

package edu.example;

public class Configuration {

    static final String BROKER_URL          = "tcp://localhost:61616";
    static final String QUEUE_NAME          = "mota.hwk06";
    static final int    TOTAL_NUMBER_MSGS   = 100;
    static final int    MAX_QUEUE_SIZE      = 10;
    static final int    PRODUCER_SLEEP_TIME = 100;
    static final int    CONSUMER_SLEEP_TIME = 250;

}
