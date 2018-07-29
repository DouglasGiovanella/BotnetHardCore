package br.com.core.callback;

import br.com.model.ClientTableRow;
import model.GenericMessage;
import model.constant.ClientStatusEnum;

/**
 * Project: BotnetHardCore
 * ClientTableRow: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 22/07/2018
 * Time: 19:28
 */
public interface OnServerCallback {

    void onClientConnected(ClientTableRow client);

    void onClientDisconnected(ClientTableRow client);

    void onMessageReceived(ClientTableRow clientTableRow, GenericMessage message);

    void onDisconnect();

    void onClientStatusChanged(ClientTableRow client, ClientStatusEnum status);
}
