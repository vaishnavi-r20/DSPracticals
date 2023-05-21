import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket server = new Socket("localhost", 2000);
        // get current time
        long currentTime = System.currentTimeMillis();
        // send time request to server
        PrintWriter out = new PrintWriter(server.getOutputStream(), true);
        out.println(currentTime);
        // receive current time from server
        BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        String response = in.readLine();
        long serverTime = Long.parseLong(response);
        // calculate clock difference
        long clockDifference = serverTime - currentTime;
        // send clock difference to server
        PrintWriter serverOut = new PrintWriter(server.getOutputStream(), true);
        serverOut.println(clockDifference);
        // receive average clock difference from server
        BufferedReader serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
        String averageClockDifference = serverIn.readLine();
        long averageDifference = Long.parseLong(averageClockDifference);
        // adjust client clock
        long adjustedTime = serverTime + averageDifference;
        System.out.println("Client adjusted time: " + adjustedTime);
        // close sockets
        server.close();
    }
}
