package com.kischang.simple_utils.awt;

import com.kischang.simple_utils.utils.OS;

import java.awt.*;

/**
 * 消息提示工具方法
 *
 * @author KisChang
 * @date 2019-03-08
 */
public class NotifyUtils {

    public static void displayMessage(String msg) {
        displayMessage(msg, TrayIcon.MessageType.INFO);
    }

    public static void displayMessage(String msg, TrayIcon.MessageType messageType) {
        displayMessage("提示", msg, messageType);
    }

    public static void displayMessage(String title, String msg, TrayIcon.MessageType messageType) {
        if(OS.isFamilyUnix()){
            System.out.println("MSG:" + msg);
        }
        if (Desktop.isDesktopSupported()){
            ToastMessage.mk(msg).display();
        }
    }

}
