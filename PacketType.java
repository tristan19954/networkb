/**
 * This class implements an enum containing all the possible Packet Type
 * handle by our MQTT broker.
 */

 /**
 * INFO0010 - Introduction to Computer Networking @Uliège
 * Second part of the assignment
 * Academic year 2021-2022
 * @author Tristan Catteeuw - s161627 | Brieuc Jamoulle - s151977
 */

public enum PacketType {
    CONNECT (1),
	CONNACK (2),
	PUBLISH (3),
	SUBSCRIBE (8),
	SUBACK (9),
	UNSUBSCRIBE (10),
	UNSUBACK (11),
	PINGREQ (12),
	PINGRESP (13),
	DISCONNECT (14);
	
	private int value;
	
	PacketType (int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public static PacketType valueOf(int value) {
		for (PacketType p : PacketType.values()) {
			if (p.value == value) return p;
		}
		throw new IllegalArgumentException("Packet Type not valid!");
	}
}
