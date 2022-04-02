package it.polimi.ingsw.javangers.server.model.game_data;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.TokenContainer;
import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a player dashboard.
 */
public class PlayerDashboard {
    /**
     * Dashboard entrance token container.
     */
    private final TokenContainer entrance;
    /**
     * Dashboard hall token container.
     */
    private final TokenContainer hall;
    /**
     * List of playable assistant cards.
     */
    private final List<AssistantCard> assistantCardsList;
    /**
     * List of discarded assistant cards.
     */
    private final List<AssistantCard> discardedAssistantCardsList;
    /**
     * Type of cards' back.
     */
    private final WizardType cardsBack;
    /**
     * Dashboard towers pair.
     */
    private Pair<TowerColor, Integer> towers;
    /**
     * Number of coins available to the player.
     */
    private int coinsNumber;

    /**
     * Constructor for player dashboard, initializing token containers, assistant cards (from json resource file), cards' back, towers and coins.
     *
     * @param assistantCardResourceLocation resource location of assistant cards json file
     * @param cardsBack                     wizard type of cards' back
     * @param towers                        initial towers pair, dependent on players number
     * @param coinsNumber                   initial coins number
     * @throws IOException          if json parsing fails for some reason (stack trace can be examined)
     * @throws NullPointerException if provided resource location cannot be used (non-existing or null)
     */
    public PlayerDashboard(String assistantCardResourceLocation, WizardType cardsBack, Pair<TowerColor, Integer> towers, int coinsNumber) throws IOException, NullPointerException {
        this.entrance = new TokenContainer();
        this.hall = new TokenContainer();
        ObjectMapper mapper = new ObjectMapper();
        try {
            File jsonFile = new File(Objects.requireNonNull(getClass().getResource(assistantCardResourceLocation)).getFile());
            this.assistantCardsList = Arrays.asList(mapper.readValue(jsonFile, AssistantCard[].class));
        } catch (IOException e) {
            throw new IOException("Error while reading assistant cards json file", e);
        } catch (NullPointerException e) {
            throw new NullPointerException("Assistant cards json file resource not found");
        }
        this.discardedAssistantCardsList = new ArrayList<>();
        this.cardsBack = cardsBack;
        this.towers = towers;
        this.coinsNumber = coinsNumber;
    }

    /**
     * Get dashboard entrance token container.
     *
     * @return entrance token container
     */
    public TokenContainer getEntrance() {
        return this.entrance;
    }

    /**
     * Get dashboard hall token container.
     *
     * @return hall token container
     */
    public TokenContainer getHall() {
        return this.hall;
    }

    /**
     * Get list of playable assistant cards.
     *
     * @return list of playable assistant cards
     */
    public List<AssistantCard> getAssistantCards() {
        return this.assistantCardsList;
    }

    /**
     * Get list of discarded assistant cards.
     *
     * @return list of discarded assistant cards
     */
    public List<AssistantCard> getDiscardedAssistantCards() {
        return this.discardedAssistantCardsList;
    }

    /**
     * Get cards' back type.
     *
     * @return cards' back type
     */
    public WizardType getCardsBack() {
        return this.cardsBack;
    }

    /**
     * Get a copy of the dashboard towers pair.
     *
     * @return pair of tower color and number
     */
    public Pair<TowerColor, Integer> getTowers() {
        return new Pair<>(this.towers.getValue0(), this.towers.getValue1());
    }

    /**
     * Set number of towers, without changing the color.
     *
     * @param towersNumber new number of towers
     */
    public void setTowersNumber(int towersNumber) {
        this.towers = this.towers.setAt1(towersNumber);
    }

    /**
     * Get number of coins available to the player.
     *
     * @return number of coins
     */
    public int getCoinsNumber() {
        return this.coinsNumber;
    }

    /**
     * Set number of coins available to the player.
     *
     * @param coinsNumber new number of coins
     */
    public void setCoinsNumber(int coinsNumber) {
        this.coinsNumber = coinsNumber;
    }
}
