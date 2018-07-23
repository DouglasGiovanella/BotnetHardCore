package br.com.pricher.server.view;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

/**
 * Created by Jeferson Machado on 02/08/2017.
 */
public class NotificationManager {

    public static void show(String message) {
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

    private void displayTray(String msg) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon");
        tray.add(trayIcon);
        trayIcon.displayMessage("BOTNET", msg, MessageType.INFO);
    }

}
