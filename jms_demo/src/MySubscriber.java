package edu.example;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MySubscriber implements MessageListener {

    private MessageConsumer consumer;
    private Session session;

    public MySubscriber() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(Configuration.BROKER_URL);
        Connection conn = factory.createConnection();
        conn.start();
        System.out.println("Consumer connected to MQ broker at " + Configuration.BROKER_URL);
        this.session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(Configuration.SOCCER_TOPIC);
        consumer = session.createConsumer(topic);
        this.consumer.setMessageListener(this);
        conn.start();
    }

    @Override
    public void onMessage(Message message) {
        System.out.println((TextMessage) message);
    }

    public static void main(String[] args) throws JMSException {
        new MySubscriber();
    }
}
