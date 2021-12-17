public class SubscribePayload {

    private final String[] topics;
    private final QoS[] qoss;

    public SubscribePayload(
            String[] topics,
            QoS[] qoss){
            this.topics = topics;
            this.qoss =  qoss;
    }

    public String[] getTopics() {
        return topics;
    }

    public QoS[] getQoss() {
        return qoss;
    }
}