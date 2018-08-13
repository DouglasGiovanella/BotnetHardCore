package br.com.client.server.view.notification;

import br.com.client.server.model.ClientTableRow;
import br.com.client.server.view.notification.animations.AnimationType;
import br.com.client.server.view.notification.models.NotificationType;
import javafx.application.Platform;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

/**
 * Created by Jeferson Machado on 02/08/2017.
 */
public class NotificationManager {

    public static void showWindowsNotification(String message) {
        if (SystemTray.isSupported()) {
            try {
                new NotificationManager().displayTray(message);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("System tray not supported!");
        }
    }

    public static void showCustomNotification(String title, String message, NotificationType type) {
        Platform.runLater(() -> {
            TrayNotification tray = new TrayNotification();
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(type);
            tray.setRectangleFill(Paint.valueOf("#2C3E50"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.setImage(new javafx.scene.image.Image("file:Server/src/br/com/resources/images/its_trap.jpg"));
            tray.showAndDismiss(Duration.seconds(5));
        });
    }

    public static void showNewClientConnected(ClientTableRow clientTableRow) {
        showCustomNotification("A new user has joined!", clientTableRow + " has joined!", NotificationType.SUCCESS);
    }

    private void displayTray(String msg) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon");
        tray.add(trayIcon);
        trayIcon.displayMessage("BotNetHardCore", msg, MessageType.INFO);
    }

}
