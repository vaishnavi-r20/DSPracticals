import java.net.*;
import java.io.*;

public class BerkeleyAlgorithm {
    public static void main(String[] args) throws Exception {
        int port = 2000; // port number
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        while (true) {
            Socket client = server.accept();
            new Thread(new ClientHandler(client)).start();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            // receive time request from client
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String request = in.readLine();
            long requestTime = Long.parseLong(request);
            // send current time to client
            long currentTime = System.currentTimeMillis();
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.println(currentTime);
            // calculate clock difference
            long clockDifference = currentTime - requestTime;
            // send clock difference to server
            Socket server = new Socket("localhost", 2000);
            PrintWriter serverOut = new PrintWriter(server.getOutputStream(), true);
            serverOut.println(clockDifference);
            // receive average clock difference from server
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String averageClockDifference = serverIn.readLine();
            long averageDifference = Long.parseLong(averageClockDifference);
            // adjust client clock
            long adjustedTime = currentTime + averageDifference;
            System.out.println("Client adjusted time: " + adjustedTime);
            // close sockets
            server.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}