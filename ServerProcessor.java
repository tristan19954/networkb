import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/*
 * This class implements a Server Processor. Its task is to process the
 * decoded messages and deal with them in accordance to the guidelines.
 */

/**
 * INFO0010 - Introduction to Computer Networking @Uliège
 * Second part of the assignment
 * Academic year 2021-2022
 * @author Tristan Catteeuw - s161627 | Brieuc Jamoulle - s151977
 */

public class ServerProcessor {

    private ServerReader reader;
    private ServerWriter writer;
    private Encoder encoder = new Encoder();
    private String clientId;
    private Storage storage;

    private final Socket s;
    private final BlockingQueue<byte []> localQueue;
    private final BlockingQueue<String> subScribeQueue;
    private final BlockingQueue<String> globalQueue;

    public ServerProcessor(
            Socket s,
            BlockingQueue<byte[]> localQueue,
            BlockingQueue<String> subScribeQueue,
            BlockingQueue<String> globalQueue,
            Storage storage) {
        this.s = s;
        this.localQueue = localQueue;
        this.subScribeQueue = subScribeQueue;
        this.globalQueue = globalQueue;
        this.storage = storage;
    }

    /**
     * The ServerReader of this processor.
     * The ServerReader uses this class as a delegate.
     *
     * @param reader    The ServerReader
     */
    public void setReader(ServerReader reader) {
        this.reader = reader;
    }

    /**
     * The ServerWriter of this processor.
     *
     * @param writer    The ServerWriter
     */
    public void setWriter(ServerWriter writer) {
        this.writer = writer;
    }

    private void setClientId(String id) {
        this.clientId = id;
    }

    public String getClientId() {
        return this.clientId;
    }

    /**
     * The main function of this class and called by the ServerReader to
     * process a decoded message. The message is processed accordingly
     * to its packet type.
     *
     * @param message   The Message to process
     */
    public void process(Message message) {
        switch (message.getFixedHeader().getPacketType()) {
            case CONNECT:
                processConnect(message);
                break;
            case PUBLISH:
                processPublish(message);
                break;
            case SUBSCRIBE:
                processSubscribe(message);
                break;
            case UNSUBSCRIBE:
                processUnsubscribe(message);
                break;
            case DISCONNECT:
                processDisconnect(message);
                break;
            case PINGREQ:
                processPingReq(message);
                break;
            default:
                System.out.println("Something unexpected happened");
        }
    }

    private synchronized void addGlobalQueue(String element){
        try {
            globalQueue.put(element);
        } catch (InterruptedException e) {
            System.out.println("Queue interrupted");
        }
    }
    /**
     * Process a CONNECT message and do the required actions.
     *
     * @param message   The message to process
     */
    private void processConnect(Message message) {
        ConnectPayload payload = (ConnectPayload) message.getPayload();
        this.setClientId(payload.getUserId());
        byte[] ackMessage = new byte[0];
        ConnectVH variable_header =  (ConnectVH)message.getVariableHeader();
        int version = variable_header.getVersion();
        if (version ==4) {
            ConnectPayload a = (ConnectPayload) message.getPayload();
            addGlobalQueue(a.getUserId());
            ackMessage = encoder.encodeConnack(false, 0);
        } else{
            ackMessage = encoder.encodeConnack(false, 1);
        }
        try {
            localQueue.put(ackMessage);
        } catch (InterruptedException e) {
            System.out.println("Queue interrupted");
        }
    }

    /**
     * Process a PUBLISH message and do the required actions.
     *
     * @param message   The message to process
     */
    private void processPublish(Message message) {
        System.out.println("this is a publish message !");
        PublishVH variable_header = (PublishVH) message.getVariableHeader();
        byte[] toTransmit = (byte[]) message.getPayload();
        String topic = variable_header.getTopicName();
        storage.transmitMessage(topic, toTransmit);
    }

    /**
     * Process a SUBSCRIBE message and do the required actions.
     *
     * @param message   The message to process
     */
    private void processSubscribe(Message message) {
        SubscribePayload a = (SubscribePayload) message.getPayload();
        String[] topics = a.getTopics();
        QoS[] qoss = a.getQoss();
        int packetIdentifier = (int) message.getVariableHeader();

        // list of subscriptions to queue
        for(int i =0; i< topics.length; i++){
            try {
                storage.addSubscribe(topics[i], clientId);
                subScribeQueue.put(topics[i]);
                System.out.println("This is subscribe Queue");
                System.out.println(subScribeQueue);
            } catch (InterruptedException e) {
                System.out.println("Queue interrupted");
            }
        }
        byte[] ackMessage = encoder.encodeSubAck(topics, qoss, packetIdentifier ,0);
        try {
            localQueue.put(ackMessage);
        } catch (InterruptedException e) {
            System.out.println("Queue interrupted");
        }
    }

    /**
     * Process a UNSUBSCRIBE message and do the required actions.
     *
     * @param message The messsage to process
     */
    private void processUnsubscribe(Message message) {
        UnsubscribePayload a = (UnsubscribePayload) message.getPayload();
        String[] topics = a.getTopics();
        int packetIdentifier = (int) message.getVariableHeader();
        for(int i =0; i< topics.length; i++){
            storage.removeSubscribe(topics[i], clientId);
            boolean removed = subScribeQueue.remove(topics[i]);
            if(!removed){
                System.out.println("An unsubscribe has been received, but the person was not subscribed");
            }
            System.out.println("This is Unsubscribe Queue");
            System.out.println(subScribeQueue);
        }
        byte[] ackMessage = encoder.encodeUnsubAck(packetIdentifier);
        try {
            localQueue.put(ackMessage);
        } catch (InterruptedException e) {
            System.out.println("Queue interrupted");
        }
    }

    /**
     * This function processes an PINGREQ message.
     * I.e, it sends back an PINGRESP message.
     *
     * @param message   The PINGREQ message
     */
    private void processPingReq(Message message) {
        byte [] pingResp = encoder.encodePingResp();

        try {
            localQueue.put(pingResp);
        } catch (InterruptedException e) {
            System.out.println("Queue interrupted");
        }
    }

    /**
     * Process a DISCONNECT message and do the required actions.
     *
     * @param message   The message to process
     */
    private void processDisconnect(Message message) {
        globalQueue.remove(this.getClientId());
        try {
            this.s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("this is a disconnect message !");
        this.reader.terminate();
        this.writer.terminate();
    }
}