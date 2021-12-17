/**
 * This class implements the SUBSCRIBE message as is a subclass of message.
 */

 /**
 * INFO0010 - Introduction to Computer Networking @Uliège
 * Second part of the assignment
 * Academic year 2021-2022
 * @author Tristan Catteeuw - s161627 | Brieuc Jamoulle - s151977
 */

public class SubscribeMessage extends Message {

    public SubscribeMessage(FixedHeader fixedHeader, Object variableHeader) {
        super(fixedHeader, variableHeader);
    }
}
