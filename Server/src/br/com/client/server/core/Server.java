package br.com.client.server.core;

import br.com.client.server.core.callback.OnClientCallback;
import br.com.client.server.core.callback.OnServerCallback;
import br.com.client.server.model.ClientTableRow;
import model.GenericMessage;
import model.constant.ClientStatusEnum;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Jeferson Machado on 26/07/2017.
 */
public class Server extends Thread {

    private OnServerCallback mCallback;
    private HashMap<ClientConnection, ClientTableRow> mClients;

    private boolean mStopServer = false;

    private ServerSocket mServerSocket;

    private Server(OnServerCallback callback, int port) throws IOException {
        mCallback = callback;
        mServerSocket = new ServerSocket(port);
        mClients = new HashMap<>();
        ClientConnection.resetClientId();
    }

    public static Server Start(OnServerCallback callback, int port) {
        try {
            Server server = new Server(callback, port);
            server.runMotherFucker();
            return server;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void runMotherFucker() {
        try {
            new Thread(() -> {

                while (true) {
                    try {
                        Socket con = mServerSocket.accept();

                        ClientConnection connection = new ClientConnection(con, new OnClientCallback() {
                            @Override
                            public void onConnectionLost(ClientConnection connection) {
                                mCallback.onClientDisconnected(mClients.get(connection));
                                mClients.remove(connection);
                            }

                            @Override
                            public void onConnectionEstablish(ClientConnection connection, ClientTableRow data) {
                                mClients.put(connection, data);
                                mCallback.onClientConnected(data);
                            }

                            @Override
                            public void onMessageReceived(ClientConnection connection, GenericMessage genericMessage) {
                                mCallback.onMessageReceived(mClients.get(connection), genericMessage);
                            }

                            @Override
                            public void onStatusUpdated(ClientConnection connection, ClientStatusEnum status) {
                                ClientTableRow client = mClients.get(connection);
                                client.setStatus(status);
                                mCallback.onClientStatusChanged(client, status);
                            }
                        });

                        connection.start();

                    } catch (Exception ignored) {
                    }

                    if (mStopServer) {
                        System.out.println("Server stopped by user");
                        mCallback.onDisconnect();
                        mStopServer = false;
                        break;
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            mCallback.onDisconnect();
        }
    }

    public void sendToAll(GenericMessage message) {
        mClients.forEach((connection, client) -> connection.sendMessage(message));
    }

    public void sendMsgToClient(ClientTableRow client, GenericMessage message) {
        if (mClients.containsValue(client)) {
            try {
                mClients.forEach(((connection, clientTableRow) -> {
                    if (clientTableRow.getId() == client.getId()) {
                        connection.sendMessage(message);
                    }
                }));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void send(GenericMessage message, ClientTableRow clientTableRow) {
        if (clientTableRow != null) {
            sendMsgToClient(clientTableRow.getId(), message);
        } else {
            sendToAll(message);
        }
    }

    public void sendMsgToClient(int clientId, GenericMessage message) {
        for (ClientConnection connection : mClients.keySet()) {

            if (mClients.get(connection).getId() == clientId) {
                sendMsgToClient(mClients.get(connection), message);
            }
        }
    }

    public void stopServer() {
        mClients.forEach((connection, client) -> connection.killEmAll());
        try {
            mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mStopServer = true;
    }
}
