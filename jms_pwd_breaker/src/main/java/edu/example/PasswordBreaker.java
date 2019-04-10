package edu.example;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class PasswordBreaker implements MessageListener {

    private PasswordGenerator pwdGen;
    private static final int PASSWORD_SIZE = 7;

    // jms additions
    public static final String BROKER_URL = "tcp://spirit.cslab.moravian.edu:61616";
    public static final String QUEUE_NAME = "mota.pwd.tasks";
    public static final String TOPIC_NAME = "mota.pwd.solution";
    private MessageProducer producer;
    private MessageConsumer consumer;
    private TopicPublisher  publisher;
    private TopicSubscriber subscriber;
    private Destination     taskQueue;
    private Topic           solutionTopic;
    private Session         session;
    private TopicSession    topicSession;

    // master/slave additions
    public static final int MASTER_ROLE = 0;
    public static final int SLAVE_ROLE  = 1;
    private int role;
    public static final int TASK_SPLIT_FACTOR = PASSWORD_SIZE - 5;

    PasswordBreaker(int size, int role) throws JMSException {
        this.role = role;

        pwdGen = new PasswordGenerator(size);
        System.out.println("Password to break is: " + pwdGen.getPassword());

        // jms additions
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        factory.setTrustAllPackages(true);

        // jms additions for producer/consumer
        Connection conn = factory.createConnection();
        conn.start();
        this.session       = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.taskQueue     = session.createQueue(QUEUE_NAME);
        this.producer      = session.createProducer(taskQueue);
        this.consumer      = session.createConsumer(taskQueue);

        // jms additions for publisher/subscriber
        TopicConnection topicConn = (TopicConnection) factory.createConnection();
        topicConn.start();
        this.topicSession  = topicConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        this.solutionTopic = topicSession.createTopic(TOPIC_NAME);
        this.publisher     = topicSession.createPublisher(solutionTopic);
        this.subscriber    = topicSession.createSubscriber(solutionTopic);
        this.subscriber.setMessageListener(this);
    }

    void search(final String password) throws JMSException {
        int size = password.length();

        // pruning
        if (size > pwdGen.getSize())
            return;

        // does the password match?
        if (size == pwdGen.getSize()) {
            if (pwdGen.match(password)) {
                // publicize
                TextMessage msg = session.createTextMessage(password);
                publisher.send(msg);
                return;
            }
        }
        // from here and on, it's a partial password
        // is the role is MASTER and the password is big enough to distribute to slaves
        else if (role == MASTER_ROLE && password.length() >= TASK_SPLIT_FACTOR) {
            TextMessage msg = session.createTextMessage(password);
            producer.send(msg);
        }
        // trip not big enough yet
        else
            for (int i = 0; i < PasswordGenerator.ALPHABET.length(); i++) {
                String tempPassword = password + PasswordGenerator.ALPHABET.charAt(i);
                search(tempPassword);
            }
    }

    @Override
    public void onMessage(Message msg) {
        try {
            String password = ((TextMessage) msg).getText();
            System.out.println("Password cracked: " + password);
            System.exit(0);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    String roleAsString() {
        return role == 0? "MASTER":"SLAVE";
    }

    void run() throws JMSException {
        System.out.println("Starting process as " + roleAsString());
        if (role == MASTER_ROLE)
            search("");
        else {
            while (true) {
                TextMessage msg = (TextMessage) consumer.receive();
                String password = msg.getText();
                System.out.println("Task: " + password);
                search(password);
            }
        }
    }

    public static void main(String[] args) throws JMSException {
        if (args.length == 0 || args[0].split("=").length != 2) {
            System.out.println("Use: java " + PasswordBreakerSingleProcess.class + " role=[0|1]");
            System.out.println("0: master; 1: slave");
            System.exit(1);
        }

        int role = MASTER_ROLE;
        try {
            role = Integer.parseInt(args[0].split("=")[1]);
            if (role < 0 || role > 1)
                throw new Exception();
        }
        catch (Exception ex) {
            System.out.println("Couldn't identify process role!");
            System.exit(1);
        }

        PasswordBreaker pwdBreaker = new PasswordBreaker(PASSWORD_SIZE, role);
        long startTime = System.nanoTime();
        pwdBreaker.run();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Running time " + duration / 1000000000. + "s");
    }
}
