package ru.tinkoff.cardgame.game.exceptions;

import ru.tinkoff.cardgame.game.model.Round;

public class RoundException extends Exception{
    public RoundException(){}
    public RoundException(String message){super(message);}
}
