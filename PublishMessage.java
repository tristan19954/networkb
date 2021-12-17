/**
 * This class implements the PUBLISH message structure.
 * It is a subclass of the Message object.
 */

 /**
 * INFO0010 - Introduction to Computer Networking @Uliège
 * Second part of the assignment
 * Academic year 2021-2022
 * @author Tristan Catteeuw - s161627 | Brieuc Jamoulle - s151977
 */

public class PublishMessage extends Message{

    public PublishMessage(FixedHeader fixedHeader, PublishVH variableHeader) {
        super(fixedHeader, variableHeader);
    }

    @Override
    public PublishVH getVariableHeader() {
        return (PublishVH) super.getVariableHeader();
    }
}
