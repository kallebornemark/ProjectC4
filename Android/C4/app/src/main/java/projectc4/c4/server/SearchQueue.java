package projectc4.c4.server;

import java.util.LinkedList;

/**
 * @author Kalle Bornemark
 */
public class SearchQueue implements Runnable {
    private LinkedList queue = new LinkedList();
    private int capacity;
    private Thread thread;
    private Server server;

    public SearchQueue(Server server, int capacity) {
        this.capacity = capacity;
        thread = new Thread(this);
    }

    public synchronized void put(ConnectedClient connectedClient) throws InterruptedException {
        while(queue.size() == capacity) {
            wait();
        }

        queue.add(connectedClient);
        start();
        notify(); // notifyAll() for multiple producer/consumer threads
    }

    public synchronized ConnectedClient get() throws InterruptedException {
        while(queue.isEmpty()) {
            wait();
        }

        ConnectedClient item = (ConnectedClient) queue.remove();
        if (queue.isEmpty()) {
            stop();
        }

        notify(); // notifyAll() for multiple producer/consumer threads
        return item;
    }

    public synchronized void remove(ConnectedClient connectedClient) throws InterruptedException {
        while(queue.isEmpty()) {
            wait();
        }

        ConnectedClient item = (ConnectedClient) queue.remove();
        if (queue.isEmpty()) {
            stop();
        }

        notify(); // notifyAll() for multiple producer/consumer threads
    }

    public void start() {
        if (thread == null) {
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                int nbr = queue.size();
                while (nbr < 2) {
                    thread.sleep(500);
                    nbr = queue.size();
                }
                server.newGame((ConnectedClient)queue.get(0), (ConnectedClient)queue.get(1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
