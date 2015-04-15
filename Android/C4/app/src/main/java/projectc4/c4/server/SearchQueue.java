package projectc4.c4.server;

import java.util.LinkedList;

/**
 * @author Kalle Bornemark
 */
public class SearchQueue implements Runnable {
    private LinkedList queue = new LinkedList();
    private int capacity;
    private Thread searchQueueListener;
    private Server server;

    public SearchQueue(Server server, int capacity) {
        this.server = server;
        this.capacity = capacity;
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
        if (searchQueueListener == null) {
            searchQueueListener = new Thread(this);
            searchQueueListener.start();
        }
    }

    public void stop() {
        if (searchQueueListener != null) {
            searchQueueListener.interrupt();
            searchQueueListener = null;
        }
    }

    public void run() {
        ConnectedClient c1;
        ConnectedClient c2;
        ActiveGame a;
        while (!Thread.interrupted()) {
            try {
                int nbr = queue.size();
                while (nbr < 2) {
                    searchQueueListener.sleep(500);
                    nbr = queue.size();
                    System.out.println("Server: nuvarande i kö: " + queue.size());
                }
                c1 = get();
                c2 = get();
                a = new ActiveGame(server, c1, c2);
                System.out.println("Server: Lägger till ett activeGame. C1 = " + c1.toString() + ", C2 = " + c2.toString());
                c1.setActiveGame(a);
                c2.setActiveGame(a);
                server.addActiveGame(a);
                stop();
                System.out.println("Server: Stoppad searchQueue tråd. kö: " + queue.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
