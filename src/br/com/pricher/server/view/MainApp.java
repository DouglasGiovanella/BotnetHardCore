package br.com.pricher.server.view;/**
 * Project: BotnetHardCore
 * Client: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 22/07/2018
 * Time: 18:42
 */

import br.com.pricher.server.controller.ClientController;
import br.com.pricher.server.core.OnServerCallback;
import br.com.pricher.server.core.Server;
import br.com.pricher.server.model.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application implements OnServerCallback {

    private Stage mPrimaryStage;
    private BorderPane mRootLayout;
    private ClientController mClientController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.mPrimaryStage = primaryStage;
        this.mPrimaryStage.setTitle("BotNet");

        initRootLayout();
        loadContent();
        Server.Start(this, 8094);
    }

    /**
     * Inicializa o root layout (layout base).
     */
    private void initRootLayout() {
        try {
            // Carrega o root layout do arquivo fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("resource/MainView.fxml"));
            mRootLayout = loader.load();

            // Mostra a scene (cena) contendo o root layout.
            Scene scene = new Scene(mRootLayout);
            mPrimaryStage.setScene(scene);
            mPrimaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra o person overview dentro do root layout.
     */
    private void loadContent() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("resource/ContentView.fxml"));
            AnchorPane personOverview = loader.load();

            // Define o person overview dentro do root layout.
            mRootLayout.setCenter(personOverview);

            mClientController = loader.getController();
            mClientController.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna o palco principal
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return mPrimaryStage;
    }

    @Override
    public void onClientConnected(Client client) {
        mClientController.add(client);
    }

    @Override
    public void onClientDisconnected(Client client) {
        mClientController.remove(client);
    }

    @Override
    public void onServerDisconnect() {

    }
}
