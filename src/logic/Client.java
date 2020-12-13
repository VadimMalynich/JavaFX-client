package logic;

import logic.Users;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private static Socket clientSocket;
    private static ObjectOutputStream outStream;
    private static ObjectInputStream inStream;
    private static String message;

    public Client() {
    }

    public Client(String ipAddress, int port) {
        try {
            clientSocket = new Socket(ipAddress, port);
            outStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Socket getClientSocket() {
        return clientSocket;
    }

    public static ObjectOutputStream getOutStream() {
        return outStream;
    }

    public static ObjectInputStream getInStream() {
        return inStream;
    }

    public static void sendMessage(String message) {
        try {
            outStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendObject(Object object) {
        try {
            outStream.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendUser(Users user) {
        try {
            outStream.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readMessage() throws IOException {
        try {
            message = (String) inStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static Object readObject() {
        Object object = new Object();
        try {
            object = inStream.readObject();
        } catch (ClassNotFoundException | IOException e) {

            e.printStackTrace();
        }
        return object;
    }

    public void close() {
        try {
            clientSocket.close();
            inStream.close();
            outStream.close();
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
