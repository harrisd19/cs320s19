public class Consumer implements MessageListener {

    private MessageConsumer consumer;
    private Session session;

    public Consumer() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(Configuration.BROKER_URL);
        Connection conn = factory.createConnection();
        conn.start();
        System.out.println("Consumer connected to MQ broker at " + Configuration.BROKER_URL);
        this.session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = this.session.createQueue(Configuration.QUEUE_NAME);
        this.consumer = this.session.createConsumer(dest);
        this.consumer.setMessageListener(this);
    }

    public void onMessage(Message original) {
        try {
            TextMessage msg = (TextMessage) original;
            System.out.println("Consumer consuming task #" + msg.getText());
        }
        catch (JMSException ex) {
            System.out.println("Ops, something went wrong: " + ex);
        }
    }

    public static void main(String[] args) {
        try {
            Consumer consumer = new Consumer();
        }
        catch (JMSException ex) {
            System.out.println("Ops, something went wrong: " + ex);
        }
    }
}
