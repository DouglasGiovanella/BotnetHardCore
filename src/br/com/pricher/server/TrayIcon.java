package br.com.pricher.server;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

/**
 * Created by Jeferson Machado on 02/08/2017.
 */
class TrayIcon {

    void displayTray(String msg) throws AWTException, java.net.MalformedURLException {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon");
        tray.add(trayIcon);
        trayIcon.displayMessage("BOTNET", msg, MessageType.INFO);
    }
}
