package br.com.pricher.server.view;/**
 * Project: BotnetHardCore
 * User: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 22/07/2018
 * Time: 18:42
 */

import br.com.pricher.server.OnServerCallback;
import br.com.pricher.server.model.User;
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.mPrimaryStage = primaryStage;
        this.mPrimaryStage.setTitle("BotNet");

        initRootLayout();
        loadContent();
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

    /**
     * Mostra o person overview dentro do root layout.
     */
    private void loadContent() {
        try {
            // Carrega o person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("ContentView.fxml"));
            AnchorPane personOverview = loader.load();

            // Define o person overview dentro do root layout.
            mRootLayout.setCenter(personOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna o palco principal.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return mPrimaryStage;
    }

    @Override
    public void onUserConnected(User user) {

    }

    @Override
    public void onUserDisconnected(int userId) {

    }

    @Override
    public void onServerDisconnect() {

    }
}
