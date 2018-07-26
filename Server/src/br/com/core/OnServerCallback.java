package br.com.core;

import br.com.model.ClientTableRow;

/**
 * Project: BotnetHardCore
 * ClientTableRow: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 22/07/2018
 * Time: 19:28
 */
public interface OnServerCallback {

    void onClientConnected(ClientTableRow client);

    void onClientDisconnected(ClientTableRow client);

    void onServerDisconnect();
}
