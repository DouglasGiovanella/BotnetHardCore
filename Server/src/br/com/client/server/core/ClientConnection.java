package br.com.client.server.core;

import br.com.client.server.core.callback.OnClientCallback;
import br.com.client.server.model.ClientTableRow;
import model.ConnectionData;
import model.GenericMessage;
import model.constant.ClientMessageTypeEnum;

import java.io.*;
import java.net.Socket;

/**
 * @Author: Douglas A. Giovanella
 * @Email: douglas_giovanella@hotmail.com
 * @Date: 28/07/2018
 */
public class ClientConnection extends Thread {

    private static int mUserGeneratedId = 0;

    private Socket mSocket;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private OnClientCallback mCallback;

    public ClientConnection(Socket socket, OnClientCallback callback) {
        mCallback = callback;
        mSocket = socket;
    }

    public static void resetClientId() {
        mUserGeneratedId = 0;
    }

    public void run() {
        System.out.println("Attempting to connect a user...");
        try {
            InputStream is = mSocket.getInputStream();
            input = new ObjectInputStream(is);
            OutputStream os = mSocket.getOutputStream();
            output = new ObjectOutputStream(os);

            ConnectionData firstMessage = (ConnectionData) input.readObject();
            System.out.println("User connected! => " + firstMessage.getName());

            ClientTableRow client = ClientTableRow.create(firstMessage, mUserGeneratedId++);

            mCallback.onConnectionEstablish(this, client);

            new Thread(() -> {
                try {
                    while (true) {
                        Object object = input.readObject();
                        if (object == null) {
                            mCallback.onConnectionLost(this);
                            break;
                        }
                        GenericMessage message = (GenericMessage) object;

                        if (message.getType() == ClientMessageTypeEnum.MESSAGE) {
                            mCallback.onMessageReceived(this, message);
                        } else {
                            mCallback.onStatusUpdated(this, message.getAsClientStatus());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("E >. " + e.getMessage());
                    mCallback.onConnectionLost(this);
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(GenericMessage message) {
        try {
            output.writeObject(message);
            output.reset();
        } catch (Exception e) {
            System.out.println("Falha ao enviar mensagem");
            e.printStackTrace();
        }
    }

    public void killEmAll() {
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stop();
        interrupt();
    }
}
