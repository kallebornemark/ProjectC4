package projectc4.c4.client;

import projectc4.c4.util.User;

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
    private User user;

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

    public void setUsername(User user) {
        this.user = user;
    }

    public void newMove(int player, int value) {
        try {
            oos.writeInt(value);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestUsername(String username) {
        try {
            oos.writeUTF(username);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startCommunication() {
        Object obj;
        try {
            while (!Thread.interrupted()) {
                obj = ois.readObject();
//                if (obj instanceof )
            }
        } catch (Exception e) {}
    }

    public void run() {
        Object obj;
        User user;
        try {
            socket = new Socket(ip, port);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            // User from server
            obj = ois.readObject();
            setUsername((User)obj);

            // Start listening
            startCommunication();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        }
    }

}
