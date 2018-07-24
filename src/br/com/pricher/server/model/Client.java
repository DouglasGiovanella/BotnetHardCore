package br.com.pricher.server.model;

import javafx.beans.property.*;

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

    public Client(int id, String name, String country, String operationSystem, String ip) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.country = new SimpleStringProperty(country);
        this.operationSystem = new SimpleStringProperty(operationSystem);
        this.ip = new SimpleStringProperty(ip);
        this.connectionTime = new SimpleObjectProperty<>(LocalDateTime.now());
    }

    public static Client create(String content, int id) {
        String[] split = content.split("&");
        return new Client(id, split[0], split[1], split[2], split[3]);
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