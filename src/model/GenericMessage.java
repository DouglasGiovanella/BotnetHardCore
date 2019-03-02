package model;

import model.constant.AttackType;
import model.constant.ClientMessageTypeEnum;
import model.constant.ClientStatusEnum;

import java.io.Serializable;

/**
 * @Author: Douglas A. Giovanella
 * @Email: douglas_giovanella@hotmail.com
 * @Date: 25/07/2018
 */
public class GenericMessage implements Serializable {

    private ClientMessageTypeEnum type;
    private AttackType attackType;
    private Object data;

    private boolean sentResponse;

    public GenericMessage(AttackType attackType, Object data, boolean sentResponse) {
        this.attackType = attackType;
        this.data = data;
        this.sentResponse = sentResponse;
    }

    public GenericMessage() {
    }

    public static GenericMessage createAttackMessage(AttackType attackType, Object data, boolean sentResponse) {
        return new GenericMessage(attackType, data, sentResponse);
    }

    public static GenericMessage createAttackMessage(AttackType attackType, Object data) {
        return new GenericMessage(attackType, data, false);
    }

    public boolean isSentResponse() {
        return sentResponse;
    }

    public void setSentResponse(boolean sentResponse) {
        this.sentResponse = sentResponse;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public void setAttackType(AttackType attackType) {
        this.attackType = attackType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ClientStatusEnum getAsClientStatus() {
        return (ClientStatusEnum) data;
    }

    public String getAsString() {
        return (String) data;
    }

    public ClientMessageTypeEnum getType() {
        return type;
    }

    public void setType(ClientMessageTypeEnum type) {
        this.type = type;
    }

    public SimplePair<String, Integer> getAsPair() {
        return (SimplePair) data;
    }

    @Override
    public String toString() {
        return "[Message received: " + data + "]" +
                " - " +
                "[MessageType: " + getType() + "]" +
                " - " +
                "[SentResponse: " + sentResponse + "]" +
                " - " +
                "[AttackType: " + attackType + "]";
    }
}
