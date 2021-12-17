import java.io.UnsupportedEncodingException;

/**
 * This class Encodes a Message object into bytes
 */

 /**
 * INFO0010 - Introduction to Computer Networking @Uliège
 * Second part of the assignment
 * Academic year 2021-2022
 * @author Tristan Catteeuw - s161627 | Brieuc Jamoulle - s151977
 */

public class Encoder {
    
    private byte[] intToByteArray(int packetId) {
        return new byte[] {
                (byte)(packetId>> 8),
                (byte) packetId};
    }

    public byte[] encodeConnack(boolean sp,  Integer returnCode) {
        byte[] message = new byte[4];
        // Always the same fixed header
        Integer firstByte = 32;
        Integer secondByte = 2;
        message[0] = firstByte.byteValue();
        message[1] = secondByte.byteValue();

        Integer spValue = 0;
        if (sp) {
            spValue = 1;
        }
        message[2] = spValue.byteValue();

        // can be 0 to 5
        message[3] = returnCode.byteValue();
        return message;
    }

    public byte[] encodeSubAck(String[] topics,  QoS[] qoss, Integer byteIdentifier, Integer returnCode) {
        byte[] message = new byte[4 + topics.length];
        Integer firstByte = 144;
        Integer secondByte = 2 + topics.length;

        message[0] = firstByte.byteValue();
        message[1] = secondByte.byteValue();

        byte[] byteIdentifierByte = intToByteArray(byteIdentifier);

        if (byteIdentifierByte.length == 2)
            {
                message[2] = byteIdentifierByte[0];
                message[3] = byteIdentifierByte[1];
            }
        else if (byteIdentifierByte.length == 1){
            Integer mlb = 0;
            message[2] =  mlb.byteValue();
            message[3] = byteIdentifierByte[0];
        } else{
            System.out.println("The byte identifier couldn't be resolved to a bytearray of length 2!");
        }

        for(int i =0; i<topics.length; i++){
            message[4 + i] = returnCode.byteValue();
        }

        return message;
    }

    public byte[] encodeUnsubAck(Integer packetIdentifier) {
        byte[] message = new byte[4];

        Integer firstByte = 160;
        Integer secondByte = 2;

        message[0] = firstByte.byteValue();
        message[1] = secondByte.byteValue();
        byte[] packet = intToByteArray(packetIdentifier);
        if ( packet.length == 2)
        {
            message[2] = packet[0];
            message[3] =  packet[1];
        }
        else if ( packet.length == 1){
            Integer mlb = 0;
            message[2] =  mlb.byteValue();
            message[3] =  packet[0];
        } else{
            System.out.println("The byte identifier couldn't be resolved to a bytearray of length 2!");
        }
        return message;
    }
    public byte[] encodePublish(String topic, byte[] payload, boolean retain, Integer packetIdentifier){
        byte[] message = new byte[2 + 2 + topic.length() + 2 + payload.length];
        Integer firstByte;
        Integer RemainingLength = 2 + topic.length() + 2 + payload.length;
        if (retain){
            firstByte = 49;
        }else{
            firstByte = 48;
        }

        message[0] = firstByte.byteValue();
        message[1] = RemainingLength.byteValue();

        Integer topicLength = topic.length();

        byte[] packet = intToByteArray(topicLength);
        if ( packet.length == 2)
        {
            message[2] = packet[0];
            message[3] =  packet[1];
        }
        else if (packet.length == 1){
            Integer mlb = 0;
            message[2] =  mlb.byteValue();
            message[3] =  packet[0];
        } else{
            System.out.println("The byte identifier couldn't be resolved to a bytearray of length 2!");
        }
        byte[] topicByte = new byte[0];
        try{
           topicByte = topic.getBytes("UTF8");
        } catch(UnsupportedEncodingException e){
            System.out.println("The byte identifier couldn't be resolved to a bytearray of length 2!");
        }
        for(int i =0; i< topicByte.length; i++){
            message[4+i] = topicByte[i];
        }

        packet = intToByteArray(packetIdentifier);
        if ( packet.length == 2)
        {
            message[4 + topicByte.length] = packet[0];
            message[5 + topicByte.length] =  packet[1];
        }
        else if ( packet.length == 1){
            Integer mlb = 0;
            message[4 + topicByte.length] =  mlb.byteValue();
            message[5 + topicByte.length] =  packet[0];
        } else{
            System.out.println("The byte identifier couldn't be resolved to a bytearray of length 2!");
        }
        for(int i = 0; i< payload.length; i++){
            message[6 + topicByte.length + i] = payload[i];
        }

        return message;
    }

    /**
     * This function encodes in byte the PINGRESP message.
     * 
     * @return  The byte array
     */
    public byte[] encodePingResp() {
        byte[] encodedMessage = new byte[2];

        Integer firstByte = 208;
        Integer secondByte = 0;

        encodedMessage[0] = firstByte.byteValue();
        encodedMessage[1] = secondByte.byteValue();

        return encodedMessage;
    }

}