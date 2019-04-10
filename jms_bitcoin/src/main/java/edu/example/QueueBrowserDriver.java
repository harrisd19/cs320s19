package edu.example;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// this illustrates how to create queue browser
public class QueueBrowserDriver {

    // jms additions
    public static final String BROKER_URL = "tcp://spirit.cslab.moravian.edu:61616";
    public static final String QUEUE_NAME = "motat.readonly.queue";
    private MessageProducer producer;
    private Queue queue;
    private Session session;
    private Map<String, TextMessage> msgById;

    // master/slave additions
    public static final int MASTER_ROLE = 0;
    public static final int SLAVE_ROLE  = 1;
    private int role;

    QueueBrowserDriver(int role) throws JMSException {
        this.role = role;
        msgById = new HashMap<>();

        // jms additions
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        factory.setTrustAllPackages(true);

        // jms additions for publisher/subscriber
        Connection conn = factory.createConnection();
        conn.start();
        this.session  = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.queue = session.createQueue(QUEUE_NAME);
        this.producer  = session.createProducer(queue);
    }

    void run() throws JMSException {
        if (role == MASTER_ROLE) {
            for (int i = 0; i < 10; i++) {
                TextMessage msg = session.createTextMessage("Message #" + (i + 1));
//                String msgId = "test:" + i;
//                msg.setJMSMessageID(msgId);
                producer.send(msg);
                String msgId = msg.getJMSMessageID();
                msgById.put(msgId, msg); // save mapping of msg ids to msgs
            }

            System.out.print("Msg Ids: ");
            System.out.println(msgById.keySet());
            System.out.println("Msg Id to delete? ");
            Scanner sc = new Scanner(System.in);
            String msgId = sc.nextLine();
//            msgId = msgId.replace("'", "\'");
            MessageConsumer consumer = session.createConsumer(queue, "JMSMessageID='" + msgId + "'");
            TextMessage msg = (TextMessage) consumer.receive();
            System.out.println("Msg deleted: " + msg.getText());
        }
        // slave code
        else {
            QueueBrowser browser = session.createBrowser(queue);
            Enumeration msgs = browser.getEnumeration();
            while (msgs.hasMoreElements()) {
                TextMessage msg = (TextMessage) msgs.nextElement();
                System.out.println(msg.getText());
            }
        }
    }

    public static void main(String[] args) throws JMSException {
        if (args.length == 0 || args[0].split("=").length != 2) {
            System.out.println("Use: java " + QueueBrowserDriver.class + " role=[0|1]");
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

        new QueueBrowserDriver(role).run();

    }
}
