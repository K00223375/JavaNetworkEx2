/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javanetworkex2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author k00223375
 */
public class JavaNetworkEx2Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Server started....awaiting connections");
        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(8111);

            int clientNo = 1; // The number of a client

            while (true) {
                // Listen for a new connection request
                Socket connectToClient = serverSocket.accept();

                // Print the new connect number on the console
                System.out.println("Start thread for client " + clientNo);

                // Find the client's host name, and IP address
                InetAddress clientInetAddress = connectToClient.getInetAddress();
                System.out.println("Client " + clientNo + "'s host name is " + clientInetAddress.getHostName());
                System.out.println("Client " + clientNo + "'s IP Address is " + clientInetAddress.getHostAddress());

                // Create a new thread for the connection
                HandleAClient thread = new HandleAClient(connectToClient);

                thread.start();// Start the new thread
                clientNo++;// Increment clientNo
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}

// Define the thread class for handling a new connection
class HandleAClient extends Thread {

    private Socket connectToClient; // A connected socket

    /**
     * Construct a thread
     */
    public HandleAClient(Socket socket) {
        connectToClient = socket;
    }

    /**
     * Implement the run() method for the thread
     */
    public void run() {
        try {
            // Create data input and output streams
            DataInputStream isFromClient = new DataInputStream(connectToClient.getInputStream());
            DataOutputStream osToClient = new DataOutputStream(connectToClient.getOutputStream());

            // Continuously serve the client
            while (true) {
                // Receive radius from the client
                double temper = isFromClient.readDouble();
                System.out.println("Temperature received from client: " + temper);
                String temperName = isFromClient.readUTF();
                
                double answer=0.0;
                double fConvert = 5/9;
                double fConvert1 = 9/5;
                
                System.out.println(temperName);
                //Compute Temperature
                switch (temperName) {
                    case "CtK":
                        answer= temper + 273.15;
                        break;
                    case "KtC":
                        answer = temper - 273.15;
                        break;
                    case "CtF":
                       answer = (temper*fConvert1)+32;
                        break;
                    case "FtC":
                        answer = (temper-32) * 5/9 ;
                        break;
                    case "KtF":
                        answer = (temper*fConvert1)-459.67;
                        break;
                    case "FtK":
                        answer = (temper+459.67)*5/9;
                        break;
                    default:
                        System.out.println("Invalid Input!!");
                        break;
                }
                
                osToClient.writeDouble(answer);

            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

}
