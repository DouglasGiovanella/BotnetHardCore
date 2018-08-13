package br.com.client.server.controller;

import br.com.client.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
}
