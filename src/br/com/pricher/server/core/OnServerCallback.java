package br.com.pricher.server.core;

import br.com.pricher.server.model.User;

/**
 * Project: BotnetHardCore
 * User: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 22/07/2018
 * Time: 19:28
 */
public interface OnServerCallback {

    void onUserConnected(User user);

    void onUserDisconnected(int userId);

    void onServerDisconnect();
}
