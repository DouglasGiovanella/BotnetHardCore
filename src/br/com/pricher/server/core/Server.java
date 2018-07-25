package br.com.pricher.server.core;

import br.com.pricher.server.messages.Message;
import br.com.pricher.server.model.Client;
import br.com.pricher.server.view.NotificationManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Jeferson Machado on 26/07/2017.
 */
public class Server extends Thread {

    private static int mUserGeneratedId = 0;
    private static OnServerCallback mCallback;
    private static HashMap<ObjectOutputStream, Client> mClients;
    private static HashSet<ObjectOutputStream> writers = new HashSet<>();

    private Socket socket;

    private ObjectInputStream input;
    private OutputStream os;
    private ObjectOutputStream output;
    private InputStream is;

    private Server(Socket socket) {
        this.socket = socket;
//        try {
//            InputStream in = socket.getInputStream();
//            InputStreamReader inr = new InputStreamReader(in);
//            bfr = new BufferedReader(inr);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void Start(OnServerCallback callback, int port) {
        mCallback = callback;
        mClients = new HashMap<>();
        try {
            final ServerSocket server = new ServerSocket(port);

            new Thread(() -> {
                while (true) {
                    try {
                        Socket con = server.accept();
                        Thread t = new Server(con);
                        t.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception ignored) {
            callback.onServerDisconnect();
        }
    }

    private static void sendToAll(String msg) {
        mClients.forEach((outputStream, client) -> {
            try {
                outputStream.writeObject(msg);
                outputStream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void sendMsgToClient(ObjectOutputStream outputStream, String message) {
        if (mClients.containsKey(outputStream)) {
            try {
                outputStream.writeObject(message);
                outputStream.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void removeUser(ObjectOutputStream outputStream) {
        try {
            NotificationManager.show(mClients.get(outputStream) + " disconnected :C");
            Client removed = mClients.remove(outputStream);
            mCallback.onClientDisconnected(removed);
        } catch (Exception ignored) {
        }
    }

    public void run() {
        System.out.println("Attempting to connect a user...");
        try {
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
            os = socket.getOutputStream();
            output = new ObjectOutputStream(os);

            Message firstMessage = (Message) input.readObject();
            System.out.println("Content -> " + firstMessage.getMsg());
            //checkDuplicateClientName(firstMessage);
            writers.add(output);

            Client client = Client.create(firstMessage, mUserGeneratedId++);
            mCallback.onClientConnected(client);
            mClients.put(output, client);

            NotificationManager.show(client + " -> connected :D");

            new Thread(() -> {
                while (true) {
                    try {
                        Message messageContent = (Message) input.readObject();
                        if (messageContent == null) {
                            removeUser(output);
                            break;
                        } else {
                            NotificationManager.show(messageContent.getMsg());
                        }
                    } catch (IOException e) {
                        removeUser(output);
                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
