package com.ccsw.ludoteca.game.model;

public class GameException extends Exception {

    public String extendedMessage;

    public GameException(String error, String extendedMessage) {
        super(error);
        this.extendedMessage = extendedMessage;
    }

    public String getExtendedMessage() {
        return extendedMessage;
    }
}
