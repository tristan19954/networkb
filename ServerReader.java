import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ServerReader implements Runnable{

    private Socket s;
    
    private Decoder decoder = new Decoder();
    private ServerProcessor processor;
    private volatile boolean running = true;

    public ServerReader(Socket s, ServerProcessor Servproc) {
        this.s = s;
        this.processor = Servproc;
        this.processor.setReader(this);
    }

    private byte[] read(InputStream in){
        byte[] received = new byte[256];
        int offset =0;
        while(true){
            try{
                int count = in.read(received, offset, received.length - offset);
                offset = offset + count;
            } catch(IOException e){
                //happens on timeout
                break;}}
        if (offset > 0){
            return received;
        }
        return new byte[0];
    }
   
    @Override
    public void run() {
        while (running) {
            try {
                InputStream in = s.getInputStream();
                s.setSoTimeout(100);
                
                // Listens to the input stream and recovers messages
                byte[] received_bytes = read(in);

                // Process the decoded message
                if (received_bytes.length > 0) {
                    // Decode the byte array into a message object
                    Message message = decoder.decode(received_bytes, s);

                    // Sends the message to the processor for processing
                    processor.process(message);
                }
            } catch (IOException e) {
                System.out.println("ServerWorker died: " + e.getMessage());
            }
        }
    }

    /**
     * Stops the thread
     */
    public void terminate() {
        running = false;
    }
}