/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: Hwk 06 - Consumer Class
 * Your name(s):
 */

package edu.example;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer {

    private MessageConsumer consumer;
    private Session         session;
    private Queue           queue;

    public Consumer() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(Configuration.BROKER_URL);
        Connection conn = factory.createConnection();
        conn.start();
        System.out.println("Consumer connected to MQ broker at " + Configuration.BROKER_URL);
        this.session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = this.session.createQueue(Configuration.QUEUE_NAME);
        this.consumer = this.session.createConsumer(queue);
    }

    // TODO: consume Configuration.TOTAL_NUMBER_MSGS, sleeping for Configuration.CONSUMER_SLEEP_TIME between messages
    public void run() throws JMSException {
        for (int i = 1; i <= Configuration.TOTAL_NUMBER_MSGS; i++) {
            TextMessage msg = (TextMessage) consumer.receive();
            System.out.println("Consumer consumed msg #" + msg.getText());
            try {
                Thread.sleep(Configuration.CONSUMER_SLEEP_TIME);
            }
            catch (InterruptedException ex) {

            }
        }
        System.out.println("Consumer done!");
    }


    public static void main(String[] args) throws JMSException {
        Consumer consumer = new Consumer();
        consumer.run();
    }
}
