package br.com.pricher.server;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Jeferson Machado on 26/07/2017.
 */
public class Servidor extends Thread {

    private static ArrayList<BufferedWriter> clientes;
    private static ServerSocket server;
    private String nome;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;

    /**
     * Método construtor
     * @param con do tipo Socket
     */
    public Servidor(Socket con) {
        this.con = con;

        try {
            //InputStream
            in = con.getInputStream();
            //InputStreamReader
            inr = new InputStreamReader(in);
            //BufferedReader
            bfr = new BufferedReader(inr);
        }catch (IOException e) {
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

            while(!"Sair".equalsIgnoreCase(msg) && msg != null) {
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
            bws = (BufferedWriter) bw;
            if(!(bwSaida == bws)) {
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

        try {
            //Cria os objetos necessários para instanciar o servidor
            JLabel lblMessage = new JLabel("Porta do Servidor:");
            JTextField txtPorta = new JTextField("12345");
            Object[] texts = {lblMessage, txtPorta};
            JOptionPane.showMessageDialog(null, texts);
            server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
            clientes = new ArrayList<BufferedWriter>();
            JOptionPane.showMessageDialog(null, "Servidor ativo na porta: " + txtPorta.getText());

            while(true) {
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                System.out.println("Cliente conectado...");
                Thread t = new Servidor(con);
                t.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
