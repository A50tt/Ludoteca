package com.ccsw.ludoteca.loan.model;

import com.ccsw.ludoteca.client.model.Client;
import com.ccsw.ludoteca.game.model.Game;

import java.time.LocalDate;

public class LoanDtoProperties {
    private LocalDate date = null;
    private Game game = null;
    private Client client = null;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
