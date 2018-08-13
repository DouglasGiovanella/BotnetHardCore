package br.com.client.server.core.callback;

import br.com.client.server.core.ClientConnection;
import br.com.client.server.model.ClientTableRow;
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
