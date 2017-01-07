package org.xcolab.entity.utils.flash;

import javax.servlet.http.HttpServletRequest;

public class AlertMessage {

    private static final FlashMessageStore MESSAGE_STORE = new FlashMessageStore();

    private final String message;
    private final Type type;

    private AlertMessage(String message, Type type) {
        super();
        this.message = message;
        this.type = type;
    }

    public static AlertMessage danger(String message) {
        return new AlertMessage(message, Type.DANGER);
    }

    public static AlertMessage warning(String message) {
        return new AlertMessage(message, Type.WARNING);
    }

    public static AlertMessage info(String message) {
        return new AlertMessage(message, Type.INFO);
    }

    public static AlertMessage success(String message) {
        return new AlertMessage(message, Type.SUCCESS);
    }

    public static AlertMessage extract(HttpServletRequest request) {
        return MESSAGE_STORE.pop(request, AlertMessage.class);
    }

    public void flash(HttpServletRequest request) {
        MESSAGE_STORE.put(request, this);
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "AlertMessage [message=" + message + ", type=" + type + "]";
    }

    public enum Type {
        DANGER,
        WARNING,
        INFO,
        SUCCESS
    }
}