import model.ConnectionData;
import model.GenericMessage;
import model.constant.ClientStatusEnum;
import model.constant.MessageTypeEnum;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;

/**
 * Created by Douglas Giovanella on 31/07/2017.
 */
public class Client {

    private static final String CONNECTED_SUCCESSFULLY = "connected successfully";
    // TODO: Ver depois
    private static ObjectOutputStream oos;
    private int mPort;
    private String mCountry;
    private String mHostName;
    private String mUsername;
    private InetAddress mIpAddress;
    private String mOperationSystemName;
    private Socket mSocket;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;

    private Client(String hostname, int port, String username, String operationSystemName, String country, InetAddress ipAddress) {
        this.mHostName = hostname;
        this.mPort = port;
        this.mUsername = username;
        this.mOperationSystemName = operationSystemName;
        this.mCountry = country;
        this.mIpAddress = ipAddress;
    }

    public static void main(String[] args) throws IOException {
        Client app = new Client(
                "localhost",
                8094,
                System.getProperty("user.name"),
                System.getProperty("os.name"),
                System.getProperty("user.country"),
                InetAddress.getLocalHost());
        app.connect();
    }

    private void connect() {
        do {
            try {
                mSocket = new Socket(mHostName, mPort);
                outputStream = mSocket.getOutputStream();
                oos = new ObjectOutputStream(outputStream);
                is = mSocket.getInputStream();
                input = new ObjectInputStream(is);
            } catch (IOException e) {
                System.err.println("Could not connect to server");
            }
        } while (mSocket == null);

        System.out.println("Connection accepted " + mSocket.getInetAddress() + ":" + mSocket.getPort());

        try {
            sendClientData();
            sendMessage(CONNECTED_SUCCESSFULLY);
            System.out.println("Sockets in and out ready!");
            while (mSocket.isConnected()) {

                GenericMessage message = (GenericMessage) input.readObject();

                if (message != null) {

                    System.out.println(message);

                    switch (message.getType()) {
                        case NOTIFICATION:
                            break;
                        case DISCONNECTED:
                            break;
                        case STATUS_UPDATE:
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /***
     * Método usado para enviar mensagem para o server mSocket
     * @param msg do tipo String
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    private void sendMessage(String msg) throws IOException {
        GenericMessage<String> createMessage = new GenericMessage<>();
        createMessage.setData(msg);
        createMessage.setType(Objects.equals(msg, CONNECTED_SUCCESSFULLY) ? MessageTypeEnum.CONNECTED : MessageTypeEnum.MESSAGE);
        oos.writeObject(createMessage);
        oos.flush();
    }

    public void sendStatusUpdate(ClientStatusEnum status) throws IOException {
        GenericMessage<ClientStatusEnum> createMessage = new GenericMessage<>();
        createMessage.setType(MessageTypeEnum.STATUS_UPDATE);
        createMessage.setData(status);
        oos.writeObject(createMessage);
        oos.flush();
    }

    /* esse método is usado para enviar a mensagem de conexao */
    private void sendClientData() throws IOException {
        ConnectionData createMessage = new ConnectionData();
        createMessage.setName(mUsername);
        createMessage.setCountry(mCountry);
        createMessage.setOperationalSystemName(mOperationSystemName);
        createMessage.setIpAddress(mIpAddress);
        oos.writeObject(createMessage);
        oos.flush();
    }
}