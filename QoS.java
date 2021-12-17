/**
 * This class implements an enum containing the different Quality of Service
 * possible.
 */

 /**
 * INFO0010 - Introduction to Computer Networking @Uliège
 * Second part of the assignment
 * Academic year 2021-2022
 * @author Tristan Catteeuw - s161627 | Brieuc Jamoulle - s151977
 */

public enum QoS {
    AT_MOST_ONCE (0),
	AT_LEAST_ONE (1),
	EXACTLY_ONE (2);
	
	private int value;
	
	QoS (int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public static QoS valueOf(int value) {
		for (QoS q : QoS.values()) {
			if (q.value == value) return q;
		}
		throw new IllegalArgumentException("Packet Type not valid!");
	}
}
