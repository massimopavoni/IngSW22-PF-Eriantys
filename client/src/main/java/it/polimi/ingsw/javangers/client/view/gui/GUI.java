package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import it.polimi.ingsw.javangers.client.view.View;

import java.util.List;

public class GUI extends View {

    /**
     * Constructor for gui, initializing directives dispatcher and parser, view and starting main thread.
     *
     * @param directivesDispatcher directives dispatcher instance
     * @param directivesParser     directives parser instance
     */
    public GUI(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser) {
        super(directivesDispatcher, directivesParser);
    }

    @Override
    public void main(String[] args) {
        GUILauncherApplication.main(args);
    }

    @Override
    protected void createGame() {

    }

    @Override
    protected void joinGame() {

    }

    @Override
    protected void waitForStart() {

    }

    @Override
    protected void startGame() {

    }

    @Override
    protected void startShow() {

    }

    @Override
    protected void updateGame() {

    }

    @Override
    protected void showAbort(String message) {

    }

    @Override
    protected void showError(String message) {

    }

    @Override
    protected void closeGame(List<String> winners) {

    }

    @Override
    protected void enableActions() {

    }

    @Override
    protected void waitTurn() {

    }

    @Override
    protected void returnToMainMenu() {

    }
}
