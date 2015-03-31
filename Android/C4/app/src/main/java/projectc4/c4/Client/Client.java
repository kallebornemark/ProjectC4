package projectc4.c4.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Kalle Bornemark
 */
public class Client implements Runnable {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String ip;
    private int port;
    private Thread client;

    public void connect(String ip, int port) {
        this.ip = ip;
        this.port = port;
        if (client == null) {
            client = new Thread(this);
            client.start();
        }
    }

    public void disconnect() {
        if (client != null) {
            client.interrupt();
            client = null;
        }
    }

    public void newMove(int player, int value) {
        try {
            oos.writeInt(value);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startCommunication() {
        try {
            while (!Thread.interrupted()) {
                // Stuff
            }
        } catch (Exception e) {}
    }

    public void run() {
        try {
            socket = new Socket(ip, port);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            // Start listening
            startCommunication();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
