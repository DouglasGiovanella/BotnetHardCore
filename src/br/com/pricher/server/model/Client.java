package br.com.pricher.server.model;

import br.com.pricher.server.messages.Message;
import javafx.beans.property.*;

import java.net.InetAddress;
import java.time.LocalDateTime;

/**
 * Project: BotnetHardCore
 * Client: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 22/07/2018
 * Time: 19:05
 */
public class Client {

    private IntegerProperty id;
    private StringProperty name;
    private StringProperty country;
    private StringProperty operationSystem;
    private StringProperty ip;
    private ObjectProperty<LocalDateTime> connectionTime;

    /**
     * Construtor padrao
     */
    public Client() {
    }

    public Client(int id, String name, String country, String operationSystem, InetAddress ip) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.country = new SimpleStringProperty(country);
        this.operationSystem = new SimpleStringProperty(operationSystem);
        this.ip = new SimpleStringProperty(ip.getHostAddress());
        this.connectionTime = new SimpleObjectProperty<>(LocalDateTime.now());
    }

    public static Client create(Message content, int id) {
        return new Client(id, content.getName(), content.getCountry(), content.getOSName(), content.getIpAddress());
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public String getOperationSystem() {
        return operationSystem.get();
    }

    public StringProperty operationSystemProperty() {
        return operationSystem;
    }

    public String getIp() {
        return ip.get();
    }

    public StringProperty ipProperty() {
        return ip;
    }

    public LocalDateTime getConnectionTime() {
        return connectionTime.get();
    }

    public ObjectProperty<LocalDateTime> connectionTimeProperty() {
        return connectionTime;
    }

    @Override
    public String toString() {
        return "[" + getName() + "]-[" + getCountry() + "][" + getIp() + "]";
    }
}
