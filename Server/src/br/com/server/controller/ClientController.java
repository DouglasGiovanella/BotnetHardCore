package br.com.server.controller;

import br.com.client.server.model.ClientTableRow;
import br.com.server.view.MainApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.constant.ClientStatusEnum;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author: Douglas A. Giovanella
 * @Email: douglas_giovanella@hotmail.com
 * @Date: 23/07/2018
 */

public class ClientController {

    //region Tabela
    @FXML
    private TableView<ClientTableRow> clientTable;
    @FXML
    private TableColumn<ClientTableRow, Integer> idColumn;
    @FXML
    private TableColumn<ClientTableRow, String> nameColumn;
    @FXML
    private TableColumn<ClientTableRow, String> countryColumn;
    @FXML
    private TableColumn<ClientTableRow, String> operationSystemColumn;
    @FXML
    private TableColumn<ClientTableRow, String> ipColumn;
    @FXML
    private TableColumn<ClientTableRow, LocalDateTime> connectionTimeColumn;
    @FXML
    private TableColumn<ClientTableRow, String> statusColumn;
    //endregion

    //region HeaderTop
    @FXML
    private TextField portTextInput;
    @FXML
    private Button startServerBtn;
    @FXML
    private Circle statusCircle;
    @FXML
    private Label clientsQuantityLabel;

    @FXML
    private AnchorPane root;
    //endregion

    private MainApp mainApp;

    public ClientController() {
    }

    @FXML
    private void initialize() {

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        countryColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
        operationSystemColumn.setCellValueFactory(cellData -> cellData.getValue().operationSystemProperty());
        ipColumn.setCellValueFactory(cellData -> cellData.getValue().ipAddressProperty());
        connectionTimeColumn.setCellValueFactory(cellData -> cellData.getValue().connectionTimeProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        for (TableColumn<ClientTableRow, ?> clientTableColumn : clientTable.getColumns()) {
            clientTableColumn.setStyle("-fx-alignment: CENTER;");
        }

        root.setOnMouseClicked(event -> clientTable.getSelectionModel().clearSelection());

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void add(ClientTableRow client) {
        Platform.runLater(() -> {
            clientTable.getItems().add(client);
            clientsQuantityLabel.setText(String.valueOf(clientTable.getItems().size()));
        });
    }

    public void remove(ClientTableRow client) {
        Platform.runLater(() -> {
            clientTable.getItems().remove(client);
            clientsQuantityLabel.setText(String.valueOf(clientTable.getItems().size()));
        });
    }

    @FXML
    private void onStartButtonClick() {
        if (Objects.equals(startServerBtn.getText(), "Iniciar")) {
            mainApp.startServer(Integer.parseInt(portTextInput.getText()));
            statusCircle.setFill(Color.LIGHTGREEN);
            startServerBtn.setText("Parar");
        } else {
            mainApp.stopServer();
            statusCircle.setFill(Color.RED);
            startServerBtn.setText("Iniciar");
        }
    }

    @FXML
    private void onAttackButtonClick() {
        ClientTableRow selectedClient = clientTable.getSelectionModel().getSelectedItem();
        // if (selectedClient != null) {
        mainApp.showClientAttackDialog(selectedClient);
        // if (okClicked) {
        //System.out.println("Atacando?");
        //Realizar uma atividade visual para o usuario. mostrando que o cliente recebeu o comando.
        //Mostrar a finailziacao tambem.
        //  }
        // } else {
        // Nada selecionado
           /* Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nenhuma seleção");
            alert.setHeaderText("Nenhum Cliente Selecionado");
            alert.setContentText("Por favor, selecione um cliente na tabela.");
            alert.showAndWait();*/
        //}
    }

    public void updateStatus(ClientTableRow client, ClientStatusEnum status) {
        Platform.runLater(() -> clientTable.getItems()
                .stream()
                .findFirst()
                .filter(x -> x.getId() == client.getId())
                .ifPresent(clientTableRow -> clientTableRow.setStatus(status)));
    }
}
