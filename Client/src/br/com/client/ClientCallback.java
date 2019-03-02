package br.com.client;

/**
 * @Author: Douglas A. Giovanella
 * @Email: douglas_giovanella@hotmail.com
 * @Date: 13/08/2018
 */
public interface ClientCallback {

    void onCommandReceived(String command);

    void onConnected();

    void onDisconnect();

    void onAttackStarted();

    void onAttackStopped();

}
