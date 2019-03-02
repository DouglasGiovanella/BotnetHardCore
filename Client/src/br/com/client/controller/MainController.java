package br.com.client.controller;

import br.com.client.MainApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Project: BotnetHardCore
 * User: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 01/08/2018
 * Time: 19:59
 */
public class MainController {

    //region HeaderTop
    @FXML
    private Circle statusCircle;
    @FXML
    private Label lblStatusCon;
    //endregion

    @FXML
    private Label lblStatusCommands;
    @FXML
    private ListView<String> lvHistorico;


    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void addCommand(String command) {
        Platform.runLater(() -> lvHistorico.getItems().add(0, command));
    }

    public void connected() {
        Platform.runLater(() -> {
            statusCircle.setFill(Color.LIGHTGREEN);
            lblStatusCon.setText("Conectado!");
        });
    }

    public void disconnect() {
        Platform.runLater(() -> {
            statusCircle.setFill(Color.RED);
            lblStatusCon.setText("Tentando conexão");
        });
    }

    public void attackStarted() {
        Platform.runLater(() -> lblStatusCommands.setText("ATACANDO!"));
    }

    public void attackStopped() {
        Platform.runLater(() -> lblStatusCommands.setText("Aguardando comando!"));
    }
}
