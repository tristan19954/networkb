/**
 * This class implements the structure of the fixed header of the MQTT
 * protocol. This strcutre is independant of the packet type.
 */

 /**
 * INFO0010 - Introduction to Computer Networking @Uliège
 * Second part of the assignment
 * Academic year 2021-2022
 * @author Tristan Catteeuw - s161627 | Brieuc Jamoulle - s151977
 */

public class FixedHeader {
    private final PacketType packetType;
	private final boolean isDup;
	private final QoS qos;
	private final boolean isRetain;
	private final int remainingLength;
	private final int fixedHeaderLength;
	
	/**
     * Constructor of the FixedHeader object
     * 
	 * @param packetType		The packet type
	 * @param isDup				Duplicate delivery of a PUBLISH Control Packet
	 * @param qos				PUBLISH Quality of Service
	 * @param isRetain			PUBLISH Retain flag
	 * @param remainingLength	Number of bytes remaining within the current packet
	 */
	public FixedHeader(
        PacketType packetType,
        boolean isDup,
        QoS qos,
        boolean isRetain,
        int remainingLength,
        int fixedHeaderLength) {
		this.packetType = packetType;
		this.isDup = isDup;
		this.qos = qos;
		this.isRetain = isRetain;
		this.remainingLength = remainingLength;
		this.fixedHeaderLength = fixedHeaderLength;
	}

	/**
	 * @return the packetType
	 */
	public PacketType getPacketType() {
		return packetType;
	}

	/**
	 * @return the isDup
	 */
	public Boolean getIsDup() {
		return isDup;
	}

	/**
	 * @return the qos
	 */
	public QoS getQos() {
		return qos;
	}

	/**
	 * @return the isRetain
	 */
	public boolean isRetain() {
		return isRetain;
	}

	/**
	 * @return the reaminingLength
	 */
	public int getRemainingLength() {
		return remainingLength;
	}

	
    /** 
     * @return int
     */
    public int getFixedHeaderLength() {
		return fixedHeaderLength;
	}

	
	
    /** 
     * @return String
     */
    @Override
	public String toString() {
		return "FixedHeader [packetType=" + packetType + ", isDup=" + isDup + ", qos=" + qos + ", isRetain=" + isRetain
				+ ", reaminingLength=" + remainingLength + "]";
	}
}
