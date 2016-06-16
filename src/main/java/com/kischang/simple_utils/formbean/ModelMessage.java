package com.kischang.simple_utils.formbean;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.Serializable;

/**
 * 用于页面展示用的提示消息
 *
 * @author KisChang
 * @version 1.0
 * @date 2015年09月29日
 * @since 1.0
 */
public class ModelMessage implements Serializable {
    private int level = 1;
    private String msg;

    public static final int Success = 0;
    public static final int Info    = 1;
    public static final int Warning = 2;
    public static final int Error   = 3;

    public ModelMessage() {
    }

    public ModelMessage(String msg) {
        this.msg = msg;
    }

    public ModelMessage(String msg, int level) {
        this.msg = msg;
        this.level = level;
    }

    public static ModelMessage mk(String msg){
        return new ModelMessage(msg);
    }
    public static ModelMessage mk(int level){
        return new ModelMessage().setLevel(level);
    }
    public static ModelMessage mk(String msg, int level){
        return new ModelMessage(msg, level);
    }

    public String getLevelStr() {
        switch (level){
            case Success:
                return "success";
            case Info:
                return "info";
            case Warning:
                return "warning";
            case Error:
                return "danger";
            default:
                return "warning";
        }
    }

    public int getLevel() {
        return level;
    }

    public ModelMessage setLevel(int level) {
        this.level = level;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ModelMessage setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ModelMessage addModel(Model model){
        model.addAttribute("msg", this);
        return this;
    }

    public ModelMessage addRedirect(RedirectAttributes ra){
        ra.addFlashAttribute("msg", this);
        return this;
    }
}
