package br.com.client;

import br.com.client.server.controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Project: BotnetHardCore
 * User: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 01/08/2018
 * Time: 20:23
 */
public class MainApp extends Application {

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
        startClient();
    }

    /**
     * Inicializa o root layout (layout base).
     */
    private void initRootLayout() {
        try {
            // Carrega o root layout do arquivo fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("./server/view/resource/MainView.fxml"));
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
            loader.setLocation(Client.class.getResource("./server/view/resource/ContentView.fxml"));
            AnchorPane personOverview = loader.load();

            // Define o person overview dentro do root layout.
            mRootLayout.setCenter(personOverview);

            mClientController = loader.getController();
            mClientController.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startClient() {
        try {
            mClient = Client.startClient();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
