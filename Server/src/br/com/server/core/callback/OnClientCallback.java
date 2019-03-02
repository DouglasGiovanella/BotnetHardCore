package br.com.server.core.callback;

import br.com.client.server.model.ClientTableRow;
import br.com.server.core.ClientConnection;
import model.GenericMessage;
import model.constant.ClientStatusEnum;

/**
 * @Author: Douglas A. Giovanella
 * @Email: douglas_giovanella@hotmail.com
 * @Date: 28/07/2018
 */
public interface OnClientCallback {

    void onConnectionLost(ClientConnection connection);

    void onConnectionEstablish(ClientConnection connection, ClientTableRow data);

    void onMessageReceived(ClientConnection connection, GenericMessage genericMessage);

    void onStatusUpdated(ClientConnection connection, ClientStatusEnum status);

}
