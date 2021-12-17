public class PublishVH {
    
    private final String topicName;
    private final int packetIdentifier;
    private final int headerLength;


    public PublishVH(String name, int id, int header_length) {
        this.topicName = name;
        this.packetIdentifier = id;
        this.headerLength = header_length;
    }
    
    /** 
     * @return String
     */
    public String getTopicName() {
        return this.topicName;
    }

    
    /** 
     * @return int
     */
    public int getPacketIdentifier() {
        return this.packetIdentifier;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    @Override
    public String toString() {
        return "{" +
                " TopicName='" + getTopicName() + "'" +
                ", Packet Identifier='" +getPacketIdentifier() + "'" +
                "}";
    }
}
