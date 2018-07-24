package br.com.pricher.server.core;

import br.com.pricher.server.model.Client;

/**
 * Project: BotnetHardCore
 * Client: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 22/07/2018
 * Time: 19:28
 */
public interface OnServerCallback {

    void onClientConnected(Client client);

    void onClientDisconnected(Client client);

    void onServerDisconnect();
}
