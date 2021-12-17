public class UnsubscribePayload {
    private final String[] topics;


    public UnsubscribePayload(
            String[] topics){
        this.topics = topics;

    }

    public String[] getTopics() {
        return topics;
    }
}
