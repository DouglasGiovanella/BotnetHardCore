package br.com.pricher.server;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Jeferson Machado on 26/07/2017.
 */
public class Servidor extends Thread {

    private static ArrayList<BufferedWriter> clients;
    private static HashMap<BufferedWriter, String> clientsNames;
    private String name;
    private Socket con;
    private BufferedReader bfr;

    private Servidor(Socket con) {
        this.con = con;
        try {
            InputStream in = con.getInputStream();
            InputStreamReader inr = new InputStreamReader(in);
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
            clients.add(bufferedWriter);
            name = msg = bfr.readLine();
            clientsNames.put(bufferedWriter, name);
            System.out.println("Cliente conectado: " + name);
            showMenu();

            while (!"Sair".equalsIgnoreCase(msg) && msg != null) {
                msg = bfr.readLine();
                sendToAll(bufferedWriter, msg);
                System.out.println(msg);
                //TODO RETIRAR CLIENT DO ARRAY
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

        for (BufferedWriter bw : clients) {
            bws = bw;
            if (!(bwSaida == bws)) {
                bw.write(name + " -> " + msg + "\r\n");
                bw.flush();
            }
        }
    }

    /***
     * Método main
     * @param args
     */
    public static void main(String[] args) {
        try {
            JLabel lblMessage = new JLabel("Porta do Servidor:");
            JTextField txtPorta = new JTextField("8094");
            Object[] texts = {lblMessage, txtPorta};
            JOptionPane.showMessageDialog(null, texts);

            final ServerSocket server = new ServerSocket(Integer.parseInt(txtPorta.getText()));

            clientsNames = new HashMap<>();
            clients = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Servidor ativo na porta: " + txtPorta.getText());

            new Thread(() -> {
                while (true) {
                    System.out.println("Aguardando conexão...");
                    Socket con = null;
                    try {
                        con = server.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Cliente conectado...");
                    Thread t = new Servidor(con);
                    t.start();
                }
            }).start();

            new Thread(Servidor::listenCommands).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void listenCommands() {
        showMenu();
        Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.println(">. ");
            int option = reader.nextInt();
        }
    }


    private static void showMenu() {
        System.out.println("-----------------------------------------");
        if (clients.size() > 0) {
            for (int i = 0; i < clients.size(); i++) {
                System.out.println("(" + i + ") -> ClientName: " + clientsNames.get(clients.get(i)));
            }
        } else {
            System.out.println("Sem clientes conectados");
        }
        System.out.println("-----------------------------------------");
    }
}
