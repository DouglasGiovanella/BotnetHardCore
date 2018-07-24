package br.com.pricher.server.controller;

import br.com.pricher.server.model.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Project: BotnetHardCore
 * User: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 24/07/2018
 * Time: 10:55
 */
public class ClientAtackDialogController {

    @FXML
    private Label clientName;
    @FXML
    private Label clientIp;
    @FXML
    private TextField TFUrl;

    private Stage mDialogStage;
    private Client mClient;
    private boolean okClicked = false;

    /**
     * Inicializa a classe controle. Este método é chamado automaticamente
     * apos o arquivo fxml ter sido carregado.
     */
    @FXML
    private void initialize() {

    }

    /**
     * Define o palco deste dialog
     *
     * @param dialogStage
     */
    public void setmDialogStage(Stage dialogStage) {
        this.mDialogStage = dialogStage;
    }

    /**
     * Define o cliente a ser atacado no dialog
     *
     * @param client
     */
    public void setClient(Client client) {
        this.mClient = client;

        clientName.setText(mClient.getName());
        clientIp.setText(mClient.getIp());
    }

    /**
     * Retorna true seo usuario clicar OK, caso contrario false.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Chamado quando o usuario clica OK.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {

            okClicked = true;
            mDialogStage.close();
        }
    }

    /**
     * Quando o usuario clica Cancel.
     */
    @FXML
    private void handleCancel() {
        mDialogStage.close();
    }

    /**
     * Valida a entrada do usuario nos campos de texto.
     *
     * @return true se a entrada é valida
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (TFUrl.getText() == null || TFUrl.getText().length() == 0) {
            errorMessage += "Url inválida!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            //Mostra a mesnagem de erro.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campos inválidos");
            alert.setHeaderText("Favor corrigir os campos inválidos");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
}
