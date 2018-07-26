package model;

import model.constant.ClientStatusEnum;
import model.constant.MessageTypeEnum;

import java.io.Serializable;

/**
 * @Author: Douglas A. Giovanella
 * @Email: douglas_giovanella@hotmail.com
 * @Date: 25/07/2018
 */
public class GenericMessage implements Serializable {

    private MessageTypeEnum type;
    private Object data;

    public MessageTypeEnum getType() {
        return type;
    }

    public void setType(MessageTypeEnum type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "[Message received:" + getAsString() + "] [MessageType:" + getType() + "]";
    }
}
