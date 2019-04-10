package edu.example;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.jms.Queue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class BitcoinMiningSimulationJMS implements MessageListener {

    public static final String FILE_NAME = "input.txt";
    private List<Task> tasks, solvedTasks;
    private Map<Task, String> taskToMsgId;
    private Random rnd;

    // jms additions
    public static final String BROKER_URL          = "tcp://spirit.cslab.moravian.edu:61616";
    public static final String TASK_QUEUE_NAME     = "mota.bitcoin.tasks";
    public static final String SOLUTION_TOPIC_NAME = "mota.bitcoin.solution";
    private MessageProducer taskProducer;
    private Queue           taskQueue;
    private Session         session;
    private TopicPublisher  solutionPublisher;
    private TopicSubscriber solutionSubscriber;
    private Topic           solutionTopic;
    private TopicSession    topicSession;

    // master/slave additions
    public static final int MASTER_ROLE = 0;
    public static final int SLAVE_ROLE  = 1;
    private int      role;
    private Task     currentTask;
    private boolean  workingOnTask;

    BitcoinMiningSimulationJMS(String fileName, int role) throws FileNotFoundException, JMSException, NoSuchAlgorithmException {
        // setup JMS connection factory
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        factory.setTrustAllPackages(true);

        // create task queue (for both master and slaves)
        Connection conn = factory.createConnection();
        conn.start();
        this.session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.taskQueue = session.createQueue(TASK_QUEUE_NAME);

        // create topic subscriber
        TopicConnection topicConn = (TopicConnection) factory.createTopicConnection();
        topicConn.start();
        this.topicSession = topicConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic solutionTopic = topicSession.createTopic(SOLUTION_TOPIC_NAME);
        this.solutionSubscriber = topicSession.createSubscriber(solutionTopic);
        this.solutionSubscriber.setMessageListener(this);

        // create list of tasks
        this.tasks = new LinkedList<>();
        this.solvedTasks = new LinkedList<>();

        // role-dependent code
        this.role = role;
        if (this.role == MASTER_ROLE) {
            // read all tasks from the input file into "tasks"
            Scanner in = new Scanner(new FileInputStream(FILE_NAME));
            while (in.hasNextLine()) {
                String line[] = in.nextLine().split(",");
                String temp[] = line[0].split(" ");
                byte data[] = new byte[temp.length];
                for (int i = 0; i < temp.length; i++)
                    data[i] = Byte.parseByte(temp[i]);
                byte leadingZeros = Byte.parseByte(line[1]);
                Task task = new Task(data, leadingZeros);
                tasks.add(task);
            }
            in.close();

            // create mapping of tasks
            taskToMsgId = new HashMap<>();

            // create task producer
            this.taskProducer = session.createProducer(taskQueue);
        }
        // code for slave
        else {
            rnd = new Random();
            currentTask = null;

            // create topic publisher
            this.solutionPublisher = topicSession.createPublisher(solutionTopic);
        }
    }

    synchronized boolean isWorkingOnTask() {
        return workingOnTask;
    }

    synchronized void setWorkingOnTask(boolean workingOnTask) {
        this.workingOnTask = workingOnTask;
    }

    void solveCurrentTask() throws NoSuchAlgorithmException, JMSException {
        setWorkingOnTask(true);
        System.out.println("Working on task: " + currentTask);
        while (isWorkingOnTask()) {
            currentTask.generateNonce();
            if (currentTask.isSolved()) {
                System.out.println("Solution found: " + currentTask);
                System.out.println();
                solvedTasks.add(currentTask);
                setWorkingOnTask(false);
                ObjectMessage msg = session.createObjectMessage();
                msg.setObject(currentTask);
                solutionPublisher.publish(msg);
                break;
            }
        }
    }

    Task getNewTask() throws JMSException {
        QueueBrowser browser = session.createBrowser(taskQueue);
        Enumeration msgs = browser.getEnumeration();
        if (!msgs.hasMoreElements())
            return null;
        while (msgs.hasMoreElements()) {
            ObjectMessage msg = (ObjectMessage) msgs.nextElement();
            Task task = (Task) msg.getObject();
            if (solvedTasks.contains(task))
                continue;
            if (!task.isSolved())
                return task;
        }
        return null;
    }

    void run() throws NoSuchAlgorithmException, JMSException {
        if (role == MASTER_ROLE) {
            for (Task task: tasks) {
                ObjectMessage msg = session.createObjectMessage();
                msg.setObject(task);
                taskProducer.send(msg);
                String msgId = msg.getJMSMessageID();
//                System.out.println("Saving task to ID: " + msgId);
                taskToMsgId.put(task, msgId); // save mapping of task to msgId
            }
        } else {
            while (true) {
                Task task = getNewTask();
                // no more tasks to solve
                if (task == null)
                    break;
                currentTask = task;
                solveCurrentTask();
            }
        }
    }

    @Override
    public void onMessage(Message message) {
        if (role == MASTER_ROLE) {
            try {
                System.out.println("Solution reported by slave process!");
                ObjectMessage msg = (ObjectMessage) message;
                Task task = (Task) msg.getObject();
                System.out.println(task);
                System.out.println("Hash: " + Arrays.toString(task.getDigest()));
                System.out.println();

                // remove task from queue using msgId
                String msgId = taskToMsgId.get(task);
//                System.out.println("Trying to remove message with ID: " + msgId);
                MessageConsumer consumer = session.createConsumer(taskQueue, "JMSMessageID='" + msgId + "'");
                consumer.receive();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                ObjectMessage taskMsg = (ObjectMessage) message;
                Task task = (Task) taskMsg.getObject();
                if (task.equals(currentTask))
                    setWorkingOnTask(false);
                solvedTasks.add(task);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException, JMSException {
        if (args.length == 0 || args[0].split("=").length != 2) {
            System.out.println("Use: java " + BitcoinMiningSimulationJMS.class + " role=[0|1]");
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

        BitcoinMiningSimulationJMS bitcoinMining = new BitcoinMiningSimulationJMS(BitcoinMiningSimulation.FILE_NAME, role);
        bitcoinMining.run();
    }
}