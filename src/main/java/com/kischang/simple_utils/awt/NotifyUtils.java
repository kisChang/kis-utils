package com.kischang.simple_utils.awt;

import java.awt.*;

/**
 * 消息提示工具方法
 *
 * @author KisChang
 * @date 2019-03-08
 */
public class NotifyUtils {

    public static void displayMessage(String msg) {
        displayMessage(msg, 2000);
    }

    public static void displayMessage(String msg, long time) {
        displayMessage(msg, time, TrayIcon.MessageType.INFO);
    }

    public static void displayMessage(String msg, long time, TrayIcon.MessageType messageType) {
        displayMessage("提示", msg, time, messageType);
    }

    public static void displayMessage(String title, String msg, long time, TrayIcon.MessageType messageType) {
        if (Desktop.isDesktopSupported()){
            ToastMessage.mk(msg, time).display();
        }else {
            System.out.println("MSG:" + msg);
        }
    }

}
