/**
 * This class implements the structure of the payload of a CONNECT message.
 */

 /**
 * INFO0010 - Introduction to Computer Networking @Uliège
 * Second part of the assignment
 * Academic year 2021-2022
 * @author Tristan Catteeuw - s161627 | Brieuc Jamoulle - s151977
 */

public final class ConnectPayload {
    
    private final String userId;
    private final String willTopic;
    private final String username;
    private final String password;
    private final byte[] willMessage;

    /**
     * Constructor of the payload object for the CONNECT message
     * 
     * @param userId        Identifies the Client to the Server
     * @param willTopic     Will topic
     * @param username      Username of the client
     * @param password      Password of the client
     * @param willMessage   Defines the Application Message that is to be published
     */
    public ConnectPayload(
        String userId,
        String willTopic,
        String username,
        String password,
        byte[] willMessage){
        this.userId = userId;
        this.willTopic = willTopic;
        this.username = username;
        this.password = password;
        this.willMessage = willMessage;
    }

    
    /** 
     * @return String
     */
    public String getUserId() {
        return userId;
    }

    
    /** 
     * @return String
     */
    public String getWillTopic() {
        return willTopic;
    }

    
    /** 
     * @return String
     */
    public String getUsername() {
        return username;
    }

    
    /** 
     * @return String
     */
    public String getPassword() {
        return password;
    }

    
    /** 
     * @return byte[]
     */
    public byte[] getWillMessage() {
        return willMessage;
    }


    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "ConnectPayload: {" +
            " userId='" + getUserId() + "'" +
            ", willTopic='" + getWillTopic() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", willMessage='" + getWillMessage() + "'" +
            "}";
    }

}
