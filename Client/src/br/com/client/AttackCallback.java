package br.com.client;

/**
 * @Author: Douglas A. Giovanella
 * @Email: douglas_giovanella@hotmail.com
 * @Date: 28/07/2018
 */
public interface AttackCallback {

    void onStarted();

    void onFinish();


    void attackStatus(String name);
    //void onError();

}
