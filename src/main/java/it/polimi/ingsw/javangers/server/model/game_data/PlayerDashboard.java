package it.polimi.ingsw.javangers.server.model.game_data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.TokenContainer;
import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a player dashboard.
 */
public class PlayerDashboard {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Dashboard entrance token container.
     */
    private final TokenContainer entrance;
    /**
     * Dashboard hall token container.
     */
    private final TokenContainer hall;
    /**
     * Map of playable assistant cards.
     */
    private final Map<String, AssistantCard> assistantCardsMap;
    /**
     * Map of discarded assistant cards.
     */
    private final Map<String, AssistantCard> discardedAssistantCardsMap;
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
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for player dashboard, initializing token containers, assistant cards (from json resource file), cards' back, towers and coins.
     *
     * @param assistantCardsResourceLocation resource location of assistant cards json file
     * @param cardsBack                      wizard type of cards' back
     * @param towers                         initial towers pair, dependent on players number
     * @param coinsNumber                    initial coins number
     * @throws IOException if json parsing fails for some reason (stack trace can be examined)
     */
    public PlayerDashboard(String assistantCardsResourceLocation, WizardType cardsBack, Pair<TowerColor, Integer> towers, int coinsNumber) throws IOException {
        this.entrance = new TokenContainer();
        this.hall = new TokenContainer();
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            File jsonFile = new File(Objects.requireNonNull(getClass().getResource(assistantCardsResourceLocation)).getFile());
            this.assistantCardsMap = jsonMapper.readValue(jsonFile, new TypeReference<Map<String, AssistantCard>>() {
            });
        } catch (IOException e) {
            throw new IOException("Error while reading assistant cards json file", e);
        }
        this.discardedAssistantCardsMap = new LinkedHashMap<>();
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
     * Get map of playable assistant cards.
     *
     * @return map of playable assistant cards
     */
    public Map<String, AssistantCard> getAssistantCards() {
        return this.assistantCardsMap;
    }

    /**
     * Get map of discarded assistant cards.
     *
     * @return map of discarded assistant cards
     */
    public Map<String, AssistantCard> getDiscardedAssistantCards() {
        return this.discardedAssistantCardsMap;
    }

    /**
     * Get last discarded card entry.
     *
     * @return last discarded card entry
     */
    public Map.Entry<String, AssistantCard> getLastDiscardedAssistantCard() {
        if (this.discardedAssistantCardsMap.isEmpty())
            return null;
        String lastDiscardedAssistantCardName = this.discardedAssistantCardsMap.keySet().toArray()[this.discardedAssistantCardsMap.size() - 1].toString();
        return new AbstractMap.SimpleEntry<>(lastDiscardedAssistantCardName, this.discardedAssistantCardsMap.get(lastDiscardedAssistantCardName));
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
    //endregion
}
