/**
 * This class implements our decoder. The task of this decoder is to decode an
 * array of byte and create a corresponding message object.
 */

 /**
 * INFO0010 - Introduction to Computer Networking @Uliège
 * Second part of the assignment
 * Academic year 2021-2022
 * @author Tristan Catteeuw - s161627 | Brieuc Jamoulle - s151977
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import  java.net.Socket;

public class Decoder {
    
	/** 
	 * Main function of the decoder. Dispatch the decoding for the three part
	 * of the message: the fixed header, the variable header and the payload.
	 * Creates then a message containing those parts.
	 * 
	 * @param buffer	The byte buffer
	 * @return Message	A message object representing the message
	 */
	public Message decode(byte[] buffer, Socket s) {
		
		FixedHeader fixedHeader = decodeFixedHeader(buffer);
		int remainingLength = fixedHeader.getRemainingLength();
		int headerLength = fixedHeader.getFixedHeaderLength();
		PacketType packetType = fixedHeader.getPacketType();

        // ---- Printing some things-----
        System.out.println("We are reading a "+ fixedHeader.getPacketType() +" message.");
        // -----------------------------

        byte[] remainingBuffer = Arrays.copyOfRange(buffer, headerLength, headerLength+remainingLength);
        Object variableHeader = decodeVariableHeader(remainingBuffer, fixedHeader, s);
		if(packetType.getValue() == 3){
			byte[] newBuffer = Arrays.copyOfRange(buffer, 0, 2+remainingLength);
			return new Message(fixedHeader, variableHeader, newBuffer);
		}
		Object payload = decodePayload(remainingBuffer, remainingLength, fixedHeader, variableHeader);
		return new Message(fixedHeader, variableHeader, payload);
    }

    
	/** 
	 * Decodes the fixed header of the message. The fixed header has the same
	 * structure for all packet types.
	 * 
	 * @param buffer		The byte buffer
	 * @return FixedHeader	The fixed header object
	 */
	private FixedHeader decodeFixedHeader(byte[] buffer) {
		String firstByteString = ByteToString(buffer[0]);
		PacketType packetType;
		try{
			// Getting Packed Type
			packetType = PacketType.valueOf(Integer.parseInt(firstByteString.substring(0,4),2));

			firstByteString = firstByteString.substring(4,firstByteString.length());

			// Getting the Flags specific to each MQTT Control Packet type
			boolean dup = (Integer.parseInt(firstByteString.substring(0,1),2)) == 1;
			QoS qos = QoS.valueOf(Integer.parseInt(firstByteString.substring(1,3),2));
			boolean retain = (Integer.parseInt(firstByteString.substring(3))) == 1;

			// Computing remaining length
			int multiplier = 1;
			int value = 0;
			int headerLength = 1;
			byte encodedByte = (byte) 128;
			while((encodedByte & 128) != 0) {
				encodedByte = buffer[headerLength];
				value += (encodedByte & 0x7F) * multiplier;
				if (multiplier > 128 * 128 * 128) {
					throw new IllegalArgumentException("Malformed Remaining Length!");
				}
				multiplier *= 128;
				headerLength += 1;
			}
			// Creating the fixed header object with the value obtained
			FixedHeader f = new FixedHeader(packetType, dup, qos, retain, value, headerLength);
			return f;
		} catch(NumberFormatException e){
			throw new NumberFormatException("This is a problem");
		}
	}


	// ==================== DECODING VARIABLE HEADER ====================

	/** 
	 * Decodes the variable header of the message. As the structure of the
	 * variable header depends of the packet type, this function sends the
	 * decoding to subfunction corresponding to the packet type.
	 * 
	 * @param buffer	The byte buffer
	 * @param header	The fixedHeader of the message
	 * @return Object	Returns of variableHeader object from the packet type
	 */
    private Object decodeVariableHeader(byte[] buffer, FixedHeader header, Socket s) {
        switch (header.getPacketType()) {
            case CONNECT:
                return decodeConnectVH(buffer, s);
            case PUBLISH:
                return decodePublishVH(buffer, header);
            case SUBSCRIBE:
                return decodeSubscribeVH(buffer);
            case UNSUBSCRIBE:
                return decodeUnsubscribeVH(buffer);
            case PINGREQ:
				return null;
            case DISCONNECT:
				return null;
            default:
                System.out.println("Wrong or Unknown packetType in decodeVariable Header");
                return null;
        }

    }

    
	/** 
	 * Decodes the variable header of the packet type CONNECT and returns and
	 * ConnectVH object representing the variable header of a CONNECT message.
	 * 
	 * @param buffer		The byte buffer
	 * @return ConnectVH	The variable header object of CONNECT messages
	 */
	private ConnectVH decodeConnectVH(byte[] buffer, Socket s) {
		
        // Decoding Protocol Name
        int length_msb = buffer[0];
		int length_lsb = buffer[1];
		if(length_msb != 0 || length_lsb !=4){
			System.out.println("Error");
			throw new IllegalArgumentException("This is the value ! Disconnecting...");
		}
		char m = (char) buffer[2]; //77
		char q = (char) buffer[3]; //81
		char t = (char) buffer[4]; //84
		char t_bis = (char) buffer[5]; //84
		String mqtt = ""+m+q+t+t_bis;//new StringBuilder().append(m).append(q).append(t).append(t_bis).toString();
		if (!mqtt.equals("MQTT")){
            System.out.println("Not conform to MQTT ! Disconnecting...");
			throw new IllegalArgumentException("Not conform to MQTT ! Disconnecting...");
		}
        
        // Decoding Protocol Level
		int version_number = buffer[6];

        // Decoding Connect Flag
		String connect_flag = ByteToString(buffer[7]);

		boolean username = ((int) connect_flag.charAt(0) - '0') ==1;
		boolean password = ((int) connect_flag.charAt(1) - '0') ==1;
		boolean willRetain = ((int) connect_flag.charAt(2) - '0') ==1;
		QoS qos = QoS.valueOf(Integer.parseInt(connect_flag.substring(3, 5)));
        
		boolean willFlag = ((int) connect_flag.charAt(5) - '0') ==1;
		boolean cleanSessionFlag = ((int) connect_flag.charAt(6) - '0') ==1;
		int reserved = (int) connect_flag.charAt(7) - '0';
		if (reserved != 0){
			System.out.println("Unexpected 'reserved' variable! Disconnecting...");
			try{
				s.close();
			}catch(IOException e){
				System.out.println("Couldn't close socket....");
			}
            throw new IllegalArgumentException("Unexpected 'reserved' variable! Disconnecting...");
		}

        // Decoding Keep Alive
		int keep_alive_flag = (((buffer[8] & 0xFF) << 8) | (buffer[9] & 0xFF));
		ConnectVH vh = new ConnectVH(mqtt, version_number, username, password, willRetain, qos, willFlag, cleanSessionFlag);
		return vh;
	}

    
	/**
	 * Decodes the variable header of the packet type PUBLISH and returns and
	 * PublishVH object representing the variable header of a PUBLISH message.
	 *  
	 * @param buffer		The byte buffer
	 * @param header		The fixedHeader of the message
	 * @return PublishVH	The variable header object of PUBLISH messages
	 */
	private PublishVH decodePublishVH(byte[] buffer, FixedHeader header){
		boolean dup = header.getIsDup();
		if(!dup)
			System.out.println("Error, Qos level 0 doesn't account for duplicates. Message discarded.");
		
		boolean retain = header.isRetain();
		
		QoS qos = header.getQos();

		if(dup)
			System.out.println("Error, Qos level 0 doesn't account for duplicates. Message discarded.");
	
		if (qos.getValue() != 0)
			System.out.println("Error, Qos level not supported");
		

		int topicLength = ((buffer[0] & 0xFF) << 8) | (buffer[1] & 0xFF);
		byte[] topicByte = Arrays.copyOfRange(buffer, 2, 2 + topicLength);
		String topic = "";
		try {
			topic = new String(topicByte, "UTF8");
		} catch(UnsupportedEncodingException e){
			System.out.println("A Topic isn't a utf 8 string");
		}

		int byteIdentifier = ((buffer[2 + topicLength] & 0xFF) << 8) | (buffer[3 + topicLength] & 0xFF);
		
        PublishVH publishVh = new PublishVH(topic, byteIdentifier, 4 + topicLength);
		return publishVh;
	}

    
	/** 
	 * Decodes the variable header of the packet type SUBSCRIBE.
	 * 
	 * @param buffer	The byte buffer
	 * @return int		TODO
	 */
	private int decodeSubscribeVH(byte[] buffer){
		return ((buffer[0] & 0xFF) << 8) | (buffer[1] & 0xFF);
	}

    
	/** 
	 * Decodes the variable header of the packet type UNSUBSCRIBE.
	 * 
	 * @param buffer	The byte buffer
	 * @return int		TODO
	 */
	private int decodeUnsubscribeVH(byte[] buffer){
		return ((buffer[0] & 0xFF) << 8) | (buffer[1] & 0xFF);
	}


	// ========================== DECODING PAYLOAD ===========================
    
	/** 
	 * Decodes the payload of the message. As the structure of the
	 * payload depends on the packet type, this function sends the
	 * decoding to subfunction corresponding to the packet type.
	 * 
	 * @param buffer			The byte buffer
	 * @param variableLength	The length of the variable part of the message
	 * @param header			The fixedHeader of the message
	 * @param variableHeader	The variableHeader of the message
	 * @return Object			The payload object of the corresponding packet type
	 */
    private Object decodePayload(byte[] buffer, int variableLength, FixedHeader header, Object variableHeader) {
        switch (header.getPacketType()) {
            case CONNECT:
                return decodeConnectPayload(buffer, (ConnectVH) variableHeader, variableLength);
				/*
            case PUBLISH:
				return decodePublishPayload(buffer);
				 */
            case SUBSCRIBE:
                return decodeSubscribePayload(buffer);
            case UNSUBSCRIBE:
            case PINGREQ:
				return null;
            case DISCONNECT:
				return null;
            default:
                System.out.println("Unknown packetType in decodepayload");
                return null;
        }
    }

    
	/** 
	 * Decodes the payload of the packet type CONNECT and returns and
	 * payload object representing the payload of a CONNECT message.
	 * 
	 * @param buffer			The byte buffer
	 * @param variableHeader	The variableHeader of the message
	 * @param variableLength	The length of the variable part
	 * @return ConnectPayload	The object representing the payload
	 */
	private ConnectPayload decodeConnectPayload(byte[] buffer, ConnectVH variableHeader, int variableLength) {
        byte[] payload = Arrays.copyOfRange(buffer, 10, variableLength);
    
        byte user_id_msb = payload[0];
        byte user_id_lsb = payload[1];

        boolean will = variableHeader.isWillFlag();
		boolean username = variableHeader.isUsername();
		boolean password = variableHeader.isPassword();
		boolean will_retain = variableHeader.isWillRetain();

		ArrayList<String> content = new ArrayList<>();
		byte[] will_message = new byte[0];
		
        for (int i =0; i< 5; i++){
			if(i ==0 || ((i ==1 && will) || (i ==3 && username) || (i ==4 && password))){
				int user_id_length = getLength(payload);
				String user_id = getFirstField(payload,user_id_length, i);
				content.add(user_id);
				payload = Arrays.copyOfRange(payload, 2 + user_id_length, payload.length);
			}
			else if(i ==2 && will){
				int user_id_length = getLength(payload);
				will_message = Arrays.copyOfRange(payload, 2, 2 + user_id_length);
				payload = Arrays.copyOfRange(payload, 2 + user_id_length, payload.length);
			}
		}

		String user_id;
		String will_topic = "";
		String username_string = "";
		String password_string = "";
		user_id = content.remove(0);
		if (will){
			will_topic = content.remove(0);
		}
		if (username){
			username_string = content.remove(0);
		}
		if (password){
			password_string = content.remove(0);
		}
		return new ConnectPayload(user_id, will_topic, username_string, password_string, will_message);
	}

    private SubscribePayload decodeSubscribePayload(byte[] buffer){
		byte[] payload = Arrays.copyOfRange(buffer, 2, buffer.length);
		int i = 0;
		int subjectLength;
		ArrayList<String> topics = new ArrayList<>();
		ArrayList<QoS> qoss = new ArrayList<>();
		while(i < payload.length-1){
			QoS qos;
			String str = "";
			subjectLength = (( payload[i] & 0xFF) << 8) | ( payload[i+1] & 0xFF);
			byte[] topicByte = Arrays.copyOfRange(payload, 2 + i, 2 + i + subjectLength);
			try {
				str += new String(topicByte, "UTF8");
			} catch(UnsupportedEncodingException e){
				System.out.println("A Topic isn't a utf 8 string");
			}
			qos = QoS.valueOf(payload[2 + i + subjectLength]);
			topics.add(str);
			qoss.add(qos);
			i += 3 + subjectLength;

		}

		String[] topicList= topics.toArray(new String[topics.size()]);
		QoS[] qosList = qoss.toArray(new QoS[qoss.size()]);
		return new SubscribePayload(topicList, qosList);
	}

    private UnsubscribePayload decodeUnsubscribePayload(byte[] buffer, int packetIdentifier){
		int i = 0;
		int subjectLength;
		ArrayList<String> topics = new ArrayList<>();
		while(i < buffer.length){
			String str = "";
			subjectLength = (( buffer[i] & 0xFF) << 8) | ( buffer[i+1] & 0xFF);
			byte[] topicByte = Arrays.copyOfRange(buffer, 2 + i, 2 + i + subjectLength);
			try {
				str += new String(topicByte, "UTF8");
			} catch(UnsupportedEncodingException e){
				System.out.println("A Topic isn't a utf 8 string");
			}
			topics.add(str);
			i = 2 + i + subjectLength;
			// Send UnsubACK with packetIdentifier
		}
		String[] topicList= topics.toArray(new String[topics.size()]);
		return new UnsubscribePayload(topicList);
	}
	/*
	private byte[] decodePublishPayload(byte[] buffer){
		return buffer;
	}
	 */

    // =======================================================================
    // ================================ UTILS ================================

    private String ByteToString(byte byteToConvert){
		return Integer.toBinaryString((byteToConvert & 0xFF) + 0x100).substring(1);
	}

	private int getLength(byte[] payload){
		byte user_id_msb = payload[0];
		byte user_id_lsb = payload[1];
		return (((user_id_msb & 0xFF) << 8) | (user_id_lsb & 0xFF));
	}

    private String getFirstField(byte[] payload,int field_length, int type){
		byte[] get_field_value = Arrays.copyOfRange(payload, 2, 2 +field_length);
		if (type ==0 && field_length > 23) {
			System.out.println("Too long user id");
		}
		String firstField  = "";
		try{
			firstField = new String(get_field_value, "UTF8");
		} catch(UnsupportedEncodingException e){
			System.out.println("Not UTF 8");
		}

		return firstField;
	}
}
