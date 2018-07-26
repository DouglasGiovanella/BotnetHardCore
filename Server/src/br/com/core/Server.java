package br.com.core;

import br.com.model.ClientTableRow;
import br.com.view.notification.NotificationManager;
import br.com.view.notification.traynotifications.animations.AnimationType;
import br.com.view.notification.traynotifications.notification.TrayNotification;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import model.ConnectionData;
import model.GenericMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Jeferson Machado on 26/07/2017.
 */
public class Server extends Thread {

    private static int mUserGeneratedId = 0;
    private static OnServerCallback mCallback;
    private static HashMap<ObjectOutputStream, ClientTableRow> mClients;

    private static boolean mStopServer = false;

    private Socket socket;

    private ObjectInputStream input;
    private OutputStream os;
    private ObjectOutputStream output;
    private InputStream is;

    private Server(Socket socket) {
        this.socket = socket;
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (mStopServer) {
                        System.out.println("Parou");
                        callback.onServerDisconnect();
                        mStopServer = false;
                        break;
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
            ClientTableRow removed = mClients.remove(outputStream);
            mCallback.onClientDisconnected(removed);
        } catch (Exception ignored) {
        }
    }

    public static void closeConnections() {

       /* mClients.forEach((oos, client) -> {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        mClients.clear();
        try {
            mServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mStopServer = true;*/
    }

    public void run() {
        System.out.println("Attempting to connect a user...");
        try {
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
            os = socket.getOutputStream();
            output = new ObjectOutputStream(os);

            ConnectionData firstMessage = (ConnectionData) input.readObject();

            ClientTableRow client = ClientTableRow.create(firstMessage, mUserGeneratedId++);
            mCallback.onClientConnected(client);
            mClients.put(output, client);

            Platform.runLater(() -> {
                TrayNotification tray = new TrayNotification();
                tray.setTitle("A new user has joined!");
                tray.setMessage(client.getName() + " has joined!");
                tray.setRectangleFill(Paint.valueOf("#2C3E50"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.setImage(new Image("file:Server/src/br/com/resources/images/its_trap.jpg"));
                tray.showAndDismiss(Duration.seconds(5));
            });

            new Thread(() -> {
                while (true) {
                    try {
                        Object object = input.readObject();
                        if (object == null) {
                            removeUser(output);
                            continue;
                        }
                        GenericMessage message = (GenericMessage) object;
                        NotificationManager.show(message.getAsString());
                    } catch (Exception e) {
                        removeUser(output);
                        break;
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }
}
