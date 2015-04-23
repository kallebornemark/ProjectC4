package projectc4.c4.server;

import java.util.LinkedList;

/**
 * A queue to handle all players that want to play a game. When the queue has two player (or more)
 * they will be set together and a game will start. The queue-thread will start when it's one client
 * in it and stop if it's empty.
 *
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
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
//        while(queue.isEmpty()) {
//            wait();
//        }
        queue.remove(connectedClient);
//
        if (queue.isEmpty()) {
            stop();
        }
//
//        notify(); // notifyAll() for multiple producer/consumer threads
    }

    public void start() {
        if (searchQueueListener == null) {
            searchQueueListener = new Thread(this);
            searchQueueListener.start();
        }
    }

    public void stop() {
        if (searchQueueListener != null) {
            searchQueueListener = null;
            System.out.println("SERVER: que stopped");
        }
    }

    public void run() {
        ConnectedClient c1;
        ConnectedClient c2;
        ActiveGame a;
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(500);

                if ( queue.size() > 1) {
                    c1 = get();
                    c2 = get();
                    a = new ActiveGame(server, c1, c2);
                    System.out.println("New ActiveGame created");
                    c1.setActiveGame(a);
                    c2.setActiveGame(a);
                    server.addActiveGame(a);
                    System.out.println("ActiveGame set to both clients and added to server's Active Games");
                }

                if (queue.size() == 0) {
                    searchQueueListener.interrupt();
                    stop();
                }
                System.out.println(queue.size() + " in queue, waiting for second...");
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("SERVER: Que interrupted");
            }
        }
    }
}
