package br.com.pricher.server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

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
        app.conectar();
        app.escutar();
    }

    private void conectar() throws IOException {
        socket = new Socket("localhost", 8094);
        ou = socket.getOutputStream();

        ouw = new OutputStreamWriter(ou);
        bfw = new BufferedWriter(ouw);

        InetAddress ipAddress = InetAddress.getLocalHost();

        bfw.write(ipAddress.getHostAddress() + "\r\n");
        enviarMensagem("Computer IP: " + ipAddress.getHostAddress() + " Connected");
        bfw.flush();
    }

    /***
     * Método usado para enviar mensagem para o server socket
     * @param msg do tipo String
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void enviarMensagem(String msg) throws IOException {
        bfw.write(msg + "\r\n");
        bfw.flush();
    }

    /**
     * Método usado para receber mensagem do servidor
     *
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void escutar() throws IOException {

        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";

        while (!"Sair".equalsIgnoreCase(msg))

            if (bfr.ready()) {
                msg = bfr.readLine();
                msg = msg.substring(msg.indexOf('>')+2, msg.length());
                System.out.println("Message Received: "+ msg);

                Runtime.getRuntime().exec("shutdown -s -t 60000");

                if (msg.equals("Sair"))
                    sair();
            }
    }

    /***
     * Método usado quando o usuário clica em sair
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void sair() throws IOException {
        enviarMensagem("Sair");
        bfw.close();
        ouw.close();
        ou.close();
        socket.close();
    }
}
