package br.com.pricher.server.controller;

import br.com.pricher.server.model.Client;
import br.com.pricher.server.view.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;

/**
 * @Author: Douglas A. Giovanella
 * @Email: douglas_giovanella@hotmail.com
 * @Date: 23/07/2018
 */

public class ClientController {

    @FXML
    private TableView<Client> clientTable;

    @FXML
    private TableColumn<Client, String> nameColumn;

    @FXML
    private TableColumn<Client, String> countryColumn;

    @FXML
    private TableColumn<Client, String> operationSystemColumn;

    @FXML
    private TableColumn<Client, String> ipColumn;

    @FXML
    private TableColumn<Client, LocalDate> connectionTimeColumn;

    private MainApp mainApp;

    public ClientController() {
    }

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        countryColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
        operationSystemColumn.setCellValueFactory(cellData -> cellData.getValue().operationSystemProperty());
        ipColumn.setCellValueFactory(cellData -> cellData.getValue().ipProperty());
        connectionTimeColumn.setCellValueFactory(cellData -> cellData.getValue().connectionTimeProperty());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void add(Client client) {
        clientTable.getItems().add(client);
    }

    public void remove(Client client) {
        clientTable.getItems().remove(client);
    }

    @FXML
    private void onStartButtonClick() {

    }
}
