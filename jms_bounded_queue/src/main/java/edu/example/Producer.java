/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: Hwk 06 - Producer Class
 * Your name(s):
 */

package edu.example;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Enumeration;

public class Producer {

    private MessageProducer producer;
    private Queue           queue;
    private Session         session;

    public Producer() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(Configuration.BROKER_URL);
        Connection conn = factory.createConnection();
        conn.start();
        System.out.println("Producer connected to MQ broker at " + Configuration.BROKER_URL);
        this.session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = this.session.createQueue(Configuration.QUEUE_NAME);
        this.producer = this.session.createProducer(queue);
    }

    // TODO: use QueueBrowser to determine and return the queue size
    int getQueueSize() throws JMSException {
        QueueBrowser browser = session.createBrowser(queue);
        Enumeration msgs = browser.getEnumeration();
        int count = 0;
        while (msgs.hasMoreElements()) {
            msgs.nextElement();
            count++;
        }
        return count;
    }

    // TODO: produce Configuration.TOTAL_NUMBER_MSGS but don't let the queue size grow > Configuration.MAX_QUEUE_SIZE;
    //  if the queue size hits Configuration.MAX_QUEUE_SIZE, sleep for Configuration.PRODUCER_SLEEP_TIME
    public void run() throws JMSException {
        for (int i = 1; i <= Configuration.TOTAL_NUMBER_MSGS; i++) {
            while (getQueueSize() >= Configuration.MAX_QUEUE_SIZE)
                try {
                    Thread.sleep(Configuration.PRODUCER_SLEEP_TIME);
                }
                catch (InterruptedException ex) {

                }
            System.out.println("Producer created msg #" + i);
            Message msg = this.session.createTextMessage("" + i);
            this.producer.send(msg);
        }
        System.out.println("Producer done!");
    }

    public static void main(String[] args) throws JMSException {
        Producer producer = new Producer();
        producer.run();
    }
}
