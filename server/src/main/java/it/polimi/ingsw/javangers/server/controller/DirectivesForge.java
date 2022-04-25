package it.polimi.ingsw.javangers.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects.EffectStrategy;
import it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions.ActionStrategy;
import it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions.ActivateCharacterCard;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Class representing the forge for directives.
 */
public class DirectivesForge {
    /**
     * Object mapper for json serialization/deserialization.
     */
    private final ObjectMapper jsonMapper;
    /**
     * Map of action strategy class names and corresponding package path.
     */
    private final Map<String, Class<? extends ActionStrategy>> actionStrategyClassMappings;
    /**
     * Map of effect strategy class names and corresponding package path.
     */
    private final Map<String, Class<? extends EffectStrategy>> effectStrategyClassMappings;

    /**
     * Constructor for the directives forge, initializing object mapper and class mappings.
     *
     * @param actionStrategyClassMappingsResourceLocation path to the json file containing action strategy class mappings
     * @param effectStrategyClassMappingsResourceLocation path to the json file containing effect strategy class mappings
     * @throws DirectivesForgeException if an error occurred while loading class mappings
     */
    public DirectivesForge(String actionStrategyClassMappingsResourceLocation, String effectStrategyClassMappingsResourceLocation) throws DirectivesForgeException {
        try {
            this.jsonMapper = new ObjectMapper();
            InputStream jsonInputStream = DirectivesForge.class.getResourceAsStream(actionStrategyClassMappingsResourceLocation);
            this.actionStrategyClassMappings = this.jsonMapper.readValue(jsonInputStream,
                    new TypeReference<Map<String, Class<? extends ActionStrategy>>>() {
                    });
            jsonInputStream = DirectivesForge.class.getResourceAsStream(effectStrategyClassMappingsResourceLocation);
            this.effectStrategyClassMappings = this.jsonMapper.readValue(jsonInputStream,
                    new TypeReference<Map<String, Class<? extends EffectStrategy>>>() {
                    });
        } catch (IOException e) {
            throw new DirectivesForgeException(String.format("Error while loading class mappings (%s)", e.getMessage()), e);
        }
    }

    /**
     * Parse json args for game manager create directive.
     *
     * @param jsonArgs json string containing the directive args
     * @return triplet containing exact players number, expert mode flag and first player info pair (wizard type and tower color)
     * @throws DirectivesForgeException if an error occurred while deserializing json args
     */
    public Triplet<Integer, Boolean, Pair<WizardType, TowerColor>> parseGameManager(String jsonArgs) throws DirectivesForgeException {
        try {
            JsonNode constructorTree = this.jsonMapper.readTree(jsonArgs);
            return new Triplet<>(
                    constructorTree.get("exactPlayersNumber").intValue(), constructorTree.get("expertMode").booleanValue(),
                    new Pair<>(
                            WizardType.valueOf(constructorTree.at("/firstPlayerInfo/wizardType").textValue()),
                            TowerColor.valueOf(constructorTree.at("/firstPlayerInfo/towerColor").textValue())
                    ));
        } catch (JsonProcessingException e) {
            throw new DirectivesForgeException(String.format("Error while deserializing game manager arguments (%s)", e.getMessage()), e);
        }
    }

    /**
     * Parse json args for add player directive.
     *
     * @param jsonArgs json string containing the directive args
     * @return pair containing the player info (wizard type and tower color)
     * @throws DirectivesForgeException if an error occurred while deserializing json args
     */
    public Pair<WizardType, TowerColor> parseAddPlayer(String jsonArgs) throws DirectivesForgeException {
        try {
            JsonNode addPlayerTree = this.jsonMapper.readTree(jsonArgs);
            return new Pair<>(
                    WizardType.valueOf(addPlayerTree.get("wizardType").textValue()),
                    TowerColor.valueOf(addPlayerTree.get("towerColor").textValue())
            );
        } catch (JsonProcessingException e) {
            throw new DirectivesForgeException(String.format("Error while deserializing add player arguments (%s)", e.getMessage()), e);
        }
    }

    /**
     * Parse json args for action execution update directive.
     *
     * @param jsonArgs json string containing the directive args
     * @return action strategy instance initialized with provided args
     * @throws DirectivesForgeException if an error occurred while deserializing json args
     */
    public ActionStrategy parseAction(String jsonArgs) throws DirectivesForgeException {
        try {
            JsonNode actionTree = this.jsonMapper.readTree(jsonArgs);
            String actionStrategyClass = actionTree.get("action").textValue();
            String actionArgs = actionTree.get("args").toString();
            if (!this.actionStrategyClassMappings.containsKey(actionStrategyClass))
                throw new DirectivesForgeException("Action strategy class not available amongst mappings");
            if (actionStrategyClass.equals(ActivateCharacterCard.class.getSimpleName())) {
                JsonNode effectTree = this.jsonMapper.readTree(actionArgs);
                String cardName = effectTree.get("effect").textValue();
                String effectArgs = effectTree.get("args").toString();
                if (!this.effectStrategyClassMappings.containsKey(cardName))
                    throw new DirectivesForgeException("Effect strategy class not available amongst mappings");
                EffectStrategy effect = effectArgs != null ? this.jsonMapper.readValue(effectArgs, this.effectStrategyClassMappings.get(cardName))
                        : this.effectStrategyClassMappings.get(cardName).getConstructor().newInstance();
                return new ActivateCharacterCard(cardName, effect);
            }
            return actionArgs != null ? this.jsonMapper.readValue(actionArgs, this.actionStrategyClassMappings.get(actionStrategyClass))
                    : this.actionStrategyClassMappings.get(actionStrategyClass).getConstructor().newInstance();
        } catch (JsonProcessingException e) {
            throw new DirectivesForgeException(String.format("Error while deserializing action arguments (%s)", e.getMessage()), e);
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new DirectivesForgeException(String.format("Error while creating no-parameters action/effect (%s)", e.getMessage()), e);
        }
    }

    /**
     * Exception for errors within directives forge class.
     */
    public static class DirectivesForgeException extends Exception {
        /**
         * DirectivesForgeException constructor with message.
         *
         * @param message message to be shown
         */
        public DirectivesForgeException(String message) {
            super(message);
        }

        /**
         * DirectivesForgeException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public DirectivesForgeException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
