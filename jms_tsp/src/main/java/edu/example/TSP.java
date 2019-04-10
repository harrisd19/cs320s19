package edu.example;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class TSP implements MessageListener {

    private short graph[][];
    private int   size;
    private Trip  bestTrip;
    private int   bestTripCost;

    public static final String RESOURCES_FOLDER  = "resources";
    public static final String FILE_NAME         = "data30.txt";
    public static final short  INFINITY          = Short.MAX_VALUE;

    // jms additions
    public static final String BROKER_URL = "tcp://spirit.cslab.moravian.edu:61616";
    public static final String QUEUE_NAME = "mota.tsp.tasks";
    public static final String TOPIC_NAME = "mota.tsp.best-trip";
    private MessageProducer producer;
    private MessageConsumer consumer;
    private TopicPublisher  publisher;
    private TopicSubscriber subscriber;
    private Destination     taskQueue;
    private Topic           bestTripTopic;
    private Session         session;
    private TopicSession    topicSession;

    // master/slave additions
    public static final int MASTER_ROLE = 0;
    public static final int SLAVE_ROLE  = 1;
    private int role;
    public static final int TASK_SPLIT_FACTOR = 5;

    TSP(String fileName, int role) throws IOException, JMSException {
        this.role = role;
        readData(fileName);
        normalize();
        bestTrip = new Trip(this.size);
        bestTripCost = INFINITY;

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
        this.bestTripTopic = topicSession.createTopic(TOPIC_NAME);
        this.publisher     = topicSession.createPublisher(bestTripTopic);
        this.subscriber    = topicSession.createSubscriber(bestTripTopic);
        this.subscriber.setMessageListener(this);
    }

    private void readData(String fileName) throws IOException {
        Scanner sc = new Scanner(new FileReader(RESOURCES_FOLDER + "/" + fileName));
        this.size = sc.nextInt();
        this.graph = new short[this.size][this.size];
        for (int i = 0; i < this.size; i++)
            for (int j = 0; j < this.size; j++) {
                graph[i][j] = sc.nextShort();
                if (graph[i][j] == -1)
                    graph[i][j] = TSP.INFINITY;
            }
        sc.close();
    }

    void printData(PrintStream out) throws IOException {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++)
                if (this.graph[i][j] == TSP.INFINITY)
                    out.print("Inf\t");
                else
                    out.print(this.graph[i][j] + "\t");
            out.println();
        }
    }

    private void normalize() {
        for (int i = 0; i < this.size; i++) {
            int min = graph[i][0];
            for (int j = 1; j < this.size; j++)
                if (graph[i][j] < min)
                    min = graph[i][j];
            for (int j = 0; j < this.size; j++)
                if (graph[i][j] != TSP.INFINITY)
                    graph[i][j] -= min;
        }
    }

    int tripCost(Trip trip) {
        int cost = 0;
        if (trip.getSize() <= 1)
            return TSP.INFINITY;
        for (int i = 0; i < trip.getSize() - 1; i++) {
            if (cost + this.graph[trip.get(i)][trip.get(i + 1)] > TSP.INFINITY)
                return TSP.INFINITY;
            cost += this.graph[trip.get(i)][trip.get(i + 1)];
        }
        if (cost + this.graph[trip.get(trip.getSize() - 1)][trip.get(0)] > TSP.INFINITY)
            return TSP.INFINITY;
        cost += this.graph[trip.get(trip.getSize() - 1)][trip.get(0)];
        return cost;
    }

    void printTrip(Trip trip, PrintStream out) throws IOException {
        String str = trip.toString();
        str += " $" + tripCost(trip);
        out.println(str);
    }

    void tripSearch(Trip trip) throws IOException, JMSException {
        int tripCost = this.tripCost(trip);

        // pruning
        if (tripCost > bestTripCost)
            return;

        // we got a solution...
        if (trip.getSize() == this.size) {
            // we want to keep it...
            if (tripCost < bestTripCost) {
                // publicize
                ObjectMessage bestTripMsg = session.createObjectMessage();
                bestTripMsg.setObject((Trip) trip.clone());
                publisher.send(bestTripMsg);
            }
        }
        // from here and on, it's a partial trip
        // is the role is MASTER and the trip is big enough to distribute to slaves
        else if (role == MASTER_ROLE && trip.getSize() >= TASK_SPLIT_FACTOR) {
            ObjectMessage tripMsg = session.createObjectMessage();
            tripMsg.setObject(trip);
            producer.send(tripMsg);
        }
        // trip not big enough yet
        else
            for (int i = 0; i < this.size; i++)
                // check whether this is a new city
                if (!trip.has(i)) {
                    Trip clone = (Trip) trip.clone();
                    clone.add(i);
                    this.tripSearch(clone);
                }
    }

    Trip getBestTrip() {
        return this.bestTrip;
    }

    int getSize() {
        return this.size;
    }

    void setBestTrip(Trip trip) throws IOException {
        int tripCost = tripCost(trip);
        if (tripCost < bestTripCost) {
            System.out.print("New best trip: ");
            this.bestTrip = trip;
            this.bestTripCost = tripCost(trip);
            printTrip(bestTrip, System.out);
        }
    }

    @Override
    public void onMessage(Message msg) {
        try {
            ObjectMessage bestTripMsg = (ObjectMessage) msg;
            setBestTrip((Trip) bestTripMsg.getObject());
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String roleAsString() {
        return role == 0? "MASTER":"SLAVE";
    }

    void run() throws IOException, JMSException {
        System.out.println("Starting process as " + roleAsString());
        if (role == MASTER_ROLE) {
            int start = 0;
            Trip trip = new Trip(this.size);
            trip.add(start);
            tripSearch(trip);
        }
        else {
            while (true) {
                ObjectMessage tripMsg = (ObjectMessage) consumer.receive();
                System.out.print("Task: " );
                Trip trip = (Trip) tripMsg.getObject();
                printTrip(trip, System.out);
                tripSearch(trip);
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException ex) {

                }
            }
        }
    }

    public static void main(String[] args) throws IOException, JMSException {
        if (args.length == 0 || args[0].split("=").length != 2) {
            System.out.println("Use: java " + TSP.class + " role=[0|1]");
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

        TSP tsp = new TSP(TSP.FILE_NAME, role);
//        tsp.printData(System.out);
        long startTime = System.nanoTime();
        tsp.run();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Best solution found in " + duration / 1000000000. + "s");
        tsp.printTrip(tsp.getBestTrip(), System.out); // print the best solution
    }
}
