package br.com.pricher.server;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeferson Machado on 26/07/2017.
 */
public class Servidor extends Thread {

    private static ArrayList<BufferedWriter> clientes;
    private static ServerSocket server;
    private static List<String> clientsNames;
    private String nome;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;

    public Servidor(Socket con) {
        this.con = con;
        try {
            //InputStream
            in = con.getInputStream();
            //InputStreamReader
            inr = new InputStreamReader(in);
            //BufferedReader
            bfr = new BufferedReader(inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método run
     */
    public void run() {
        try {
            String msg;
            OutputStream outPutStream = this.con.getOutputStream();
            Writer writer = new OutputStreamWriter(outPutStream);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            clientes.add(bufferedWriter);
            nome = msg = bfr.readLine();
            System.out.println("Cliente conectado: " + nome);
            //clientsNames.add(nome);
            //showConnectedClients();

            while (!"Sair".equalsIgnoreCase(msg) && msg != null) {
                msg = bfr.readLine();
                sendToAll(bufferedWriter, msg);
                System.out.println(msg);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Método usado para enviar mensagem para todos os clientes
     * @param bwSaida do tipo BufferedWriter
     * @param msg do tipo String
     * @throws IOException
     */
    public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException {
        BufferedWriter bws;

        for (BufferedWriter bw : clientes) {
            bws = bw;
            if (!(bwSaida == bws)) {
                bw.write(nome + " -> " + msg + "\r\n");
                bw.flush();
            }
        }
    }

    /***
     * Método main
     * @param args
     */
    public static void main(String[] args) {
        clientsNames = new ArrayList<>();
        try {
            //Cria os objetos necessários para instanciar o servidor
            JLabel lblMessage = new JLabel("Porta do Servidor:");
            JTextField txtPorta = new JTextField("8094");
            Object[] texts = {lblMessage, txtPorta};
            JOptionPane.showMessageDialog(null, texts);
            server = new ServerSocket(Integer.parseInt(txtPorta.getText()));

            final ServerSocket gambiarra = server;

            clientes = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Servidor ativo na porta: " + txtPorta.getText());

            new Thread(() -> {
                while (true) {
                    System.out.println("Aguardando conexão...");
                    Socket con = null;
                    try {
                        con = gambiarra.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Cliente conectado...");
                    Thread t = new Servidor(con);
                    t.start();
                }
            }).start();

            new Thread(() -> {
                while (true) {


                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showConnectedClients() {
        if (clientsNames.size() != 0) {

            for (int i = 0; i < clientsNames.size(); i++) {
                System.out.println("Cliente " + i + " : " + clientsNames.get(i));
            }

        }
    }


}
