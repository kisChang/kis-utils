package com.kischang.simple_utils.awt;

import com.sun.awt.AWTUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * swing实现Toast
 * ToastMessage.mk(text, time, callBack);
 */
public class ToastMessage extends JFrame {

    public static Theme theme = Theme.White;

    public static Theme getTheme(){
        return theme == null ? Theme.White : theme;
    }

    private long time;
    private ToastEnd endCall;
    private JLabel msgLabel;
    private JPanel jPanel;

    ToastMessage(final String message, long time) {
        //之前需要调用一下
        this.time = time;

        JFrame frame = this;
        frame.setUndecorated(true);//取消边框
        frame.setAlwaysOnTop(true);//置顶
        frame.setFocusable(false);//不获取焦点
        frame.setType(Type.UTILITY);//不显示在任务栏


        //基础布局
        this.jPanel = new JPanel(); //核心用Panel
        this.jPanel.setLayout(new FlowLayout());
        this.jPanel.setBorder(new EmptyBorder(10, 20, 10, 20)); // 设置边距
        this.add(this.jPanel);

        //大小和内容
        //frame.setSize(300, 0);

        this.msgLabel = new JLabel();
        this.msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.msgLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 18));
        this.msgLabel.setSize(290,0);

        labelSetText(this.msgLabel, message);
        jPanel.add(this.msgLabel);

        //设置样式
        switch (getTheme()){
            case Black:
                this.setBackground(new Color(63, 63, 63, 150));
                jPanel.setBackground(new Color(63, 63, 63, 150));
                this.msgLabel.setForeground(new Color(240, 240, 240, 250));
                break;
            case White:
            default:
                setBackground(new Color(240, 240, 240, 250));
                jPanel.setBackground(new Color(240, 240, 240, 250));
                this.msgLabel.setForeground(new Color(0, 0, 0, 250));
                break;
        }

        //重新计算大小
        frame.pack();

        //设置显示位置
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        //居中
        int x = (int) (toolkit.getScreenSize().getWidth() - frame.getWidth()) / 2;
        //屏幕 4/5 处
        int y = (int) toolkit.getScreenSize().getHeight() / 5 * 4;
        frame.setLocation(x, y);


        //设置圆角
        AWTUtilities.setWindowShape(frame, new RoundRectangle2D.Double(0, 0, getWidth(),
                getHeight(), 20, 20));

    }

    void labelSetText(JLabel jLabel, String longString) {
        StringBuilder builder = new StringBuilder("<html>");
        char[] chars = longString.toCharArray();
        FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
        int start = 0;
        int len = 0;
        while (start + len < longString.length()) {
            while (true) {
                len++;
                if (start + len > longString.length())break;
                if (fontMetrics.charsWidth(chars, start, len)
                        > jLabel.getWidth()) {
                    break;
                }
            }
            builder
                    .append("<p align=\"center\">")
                    .append(chars, start, len - 1)
                    .append("<br/></p>");
            start = start + len - 1;
            len = 0;
        }
        builder.append(chars, start, longString.length()-start);
        builder.append("</html>");
        jLabel.setText(builder.toString());
    }

    public static enum Theme {
        Black,
        White;
    }

    //结束后的调用
    public interface ToastEnd {

        void onFinish();

    }

    ToastMessage onHided(ToastEnd toastEnd) {
        this.endCall = toastEnd;
        return this;
    }

    public void display() {
        new Thread(() ->{
            try {
                setOpacity(1);
                setVisible(true);
                Thread.sleep(this.time);

                //hide the toast message in slow motion
                for (double d = 1.0; d > 0.2; d -= 0.05) {
                    Thread.sleep(50);
                    setOpacity((float) d);
                }

                // set the visibility to false
                setVisible(false);
                this.dispose();

                if (this.endCall != null) {
                    this.endCall.onFinish();
                }
            } catch (Exception ignored) {
            }
        }).start();
    }


    public static ToastMessage mk(String text) {
        return mk(text, 2000);
    }

    public static ToastMessage mk(String text, long time) {
        return mk(text, time, null);
    }

    public static ToastMessage mk(String text, long time, ToastEnd callBack) {
        return new ToastMessage(text, time)
                .onHided(callBack);
    }
}