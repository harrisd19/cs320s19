public class Producer {

    private MessageProducer producer;
    private Session session;
    private int numberTasks;

    public Producer(int numberTasks) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(Configuration.BROKER_URL);
        Connection conn = factory.createConnection();
        conn.start();
        System.out.println("Producer connected to MQ broker at " + Configuration.BROKER_URL);
        this.session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = this.session.createQueue(Configuration.QUEUE_NAME);
        this.producer = this.session.createProducer(dest);
        if (numberTasks <= 0)
            this.numberTasks = Configuration.DEFAULT_NUMBER_TASKS;
        else
            this.numberTasks = numberTasks;
    }

    public void run() throws JMSException {
        for (int i = 1; i <= this.numberTasks; i++) {
            System.out.println("Producer created task #" + i);
            Message msg = this.session.createTextMessage("" + i);
            this.producer.send(msg);
        }
    }

    public static void main(String[] args) {
        try {
            Producer producer = new Producer(5);
            producer.run();
        }
        catch (JMSException ex) {
            System.out.println("Ops, something went wrong: " + ex);
        }
    }
}
