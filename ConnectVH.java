/**
 * This class implements the variable header structure of a CONNECT message.
 */

 /**
 * INFO0010 - Introduction to Computer Networking @Uliège
 * Second part of the assignment
 * Academic year 2021-2022
 * @author Tristan Catteeuw - s161627 | Brieuc Jamoulle - s151977
 */

public class ConnectVH {
    
    private final String protocol;
    private final int version;
    private final boolean username;
    private final boolean password;
    private final boolean willRetain;
    private final QoS qos;
    private final boolean willFlag;
    private final boolean cleanSessionFlag;

    /**
     * Constructor of a CONNECT variable header object.
     * 
     * @param protocol      The protocol name
     * @param version       Revision level of the protocol used by the Client
     * @param username      The user name of the client
     * @param password      The password of the client
     * @param willRetain    Specifies if the Will Message is to be Retained when it is published
     * @param qos           Specify the QoS level to be used when publishing the Will Message
     * @param willFlag      Specifies if the Will Message is to be Retained when it is published
     * @param cleanSessionFlag TODO
     */
    public ConnectVH(
        String protocol,
        int version,
        boolean username,
        boolean password,
        boolean willRetain,
        QoS qos,
        boolean willFlag,
        boolean cleanSessionFlag){
        this.protocol = protocol;
        this.version = version;
        this.username = username;
        this.password = password;
        this.willRetain = willRetain;
        this.qos = qos;
        this.willFlag = willFlag;
        this.cleanSessionFlag = cleanSessionFlag;
    }

    
    /** 
     * Getter of the protocol variable
     * 
     * @return String
     */
    public String getProtocol() {
        return protocol;
    }

    
    /** 
     * Getter of the version variable
     * 
     * @return int
     */
    public int getVersion() {
        return version;
    }

    
    /** 
     * Getter of the username variable
     * 
     * @return boolean
     */
    public boolean isUsername() {
        return username;
    }

    
    /**
     * Getter of the password variable
     * 
     * @return boolean
     */
    public boolean isPassword() {
        return password;
    }

    
    /**
     * Getter of the will retain
     * 
     * @return boolean
     */
    public boolean isWillRetain() {
        return willRetain;
    }

    
    /** 
     * Getter of the QoS
     * 
     * @return QoS
     */
    public QoS getQos() {
        return qos;
    }

    
    /** 
     * Getter of the will flag
     * 
     * @return boolean
     */
    public boolean isWillFlag() {
        return willFlag;
    }

    
    /** 
     * Getter of the clean session flag
     * 
     * @return boolean
     */
    public boolean isCleanSessionFlag() {
        return cleanSessionFlag;
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
            " protocol='" + getProtocol() + "'" +
            ", version='" + getVersion() + "'" +
            ", username='" + isUsername() + "'" +
            ", password='" + isPassword() + "'" +
            ", willRetain='" + isWillRetain() + "'" +
            ", qos='" + getQos() + "'" +
            ", willFlag='" + isWillFlag() + "'" +
            ", cleanSessionFlag='" + isCleanSessionFlag() + "'" +
            "}";
    }
}
