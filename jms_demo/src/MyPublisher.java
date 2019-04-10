package edu.example;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MyPublisher {

    private MessageProducer messageProducer;
    private Session session;

    public MyPublisher() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(Configuration.BROKER_URL);
        Connection conn = factory.createConnection();
        conn.start();
        System.out.println("Publisher connected to MQ broker at " + Configuration.BROKER_URL);
        this.session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(Configuration.SOCCER_TOPIC);
        messageProducer = session.createProducer(topic);
    }

    public void run() throws JMSException {
        Message msg = this.session.createTextMessage("Hello soccer fans!");
        messageProducer.send(msg);
        msg = this.session.createTextMessage("Brazil does not play as it use to...");
        messageProducer.send(msg);
    }

    public static void main(String[] args) throws JMSException {
        new MyPublisher().run();
    }

}
