/**
 * This class implement the CONNECT message structure.
 * It is a subclass of Message.
 */

 /**
 * INFO0010 - Introduction to Computer Networking @Uliège
 * Second part of the assignment
 * Academic year 2021-2022
 * @author Tristan Catteeuw - s161627 | Brieuc Jamoulle - s151977
 */

public class ConnectMessage extends Message {

    public ConnectMessage(
        FixedHeader fixedHeader,
        ConnectVH variableHeader,
        ConnectPayload payload) {
        super(fixedHeader, variableHeader, payload);
    }

    @Override
    public ConnectVH getVariableHeader() {
        return (ConnectVH) super.getVariableHeader();
    }

    @Override
    public ConnectPayload getPayload() {
        return (ConnectPayload) super.getPayload();
    }
}