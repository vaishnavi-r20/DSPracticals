import java.util.*;

public class TokenRing {
    private static final int N = 5; // Number of processes
    private static final int TOKEN = -1; // Token value
    private static final int CS_TIME = 1000; // Critical section time
    private static boolean[] hasToken = new boolean[N]; // Whether process i has the token
    private static boolean[] inCS = new boolean[N]; // Whether process i is in the critical section
    private static int tokenHolder = -1; // Current token holder

    private static void process(int id) throws InterruptedException {
        while (true) {
            if (hasToken[id]) {
                // Enter critical section
                inCS[id] = true;
                System.out.println("Process " + id + " entering critical section...");
                Thread.sleep(CS_TIME);
                System.out.println("Process " + id + " exiting critical section.");
                // Release token
                hasToken[id] = false;
                int nextId = (id + 1) % N;
                hasToken[nextId] = true;
                tokenHolder = nextId;
            } else {
                // Wait for token
                Thread.sleep(100);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Initialize token holder
        hasToken[0] = true;
        tokenHolder = 0;
        // Start processes
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int id = i;
            Thread thread = new Thread(() -> {
                try {
                    process(id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threads.add(thread);
            thread.start();
        }
        // Wait for processes to finish
        for (Thread thread : threads) {
            thread.join();
        }
    }
}