package it.polimi.ingsw.javangers.client.controller.directives;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.client.controller.MessageHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Class representing the directives dispatcher.
 */
public class DirectivesDispatcher {
    /**
     * Resource location for directives types mappings json file.
     */
    private static final String DIRECTIVES_TYPES_MAPPINGS_RESOURCE_LOCATION = "/it/polimi/ingsw/javangers/client/controller/directives/directives_types_mappings.json";
    /**
     * Resource location for directives actions mappings json file.
     */
    private static final String DIRECTIVES_ACTIONS_MAPPINGS_RESOURCE_LOCATION = "/it/polimi/ingsw/javangers/client/controller/directives/directives_actions_mappings.json";
    /**
     * Resource location for directives actions effects json file.
     */
    private static final String DIRECTIVES_EFFECTS_MAPPINGS_RESOURCE_LOCATION = "/it/polimi/ingsw/javangers/client/controller/directives/directives_effects_mappings.json";
    /**
     * Directives dispatcher singleton instance.
     */
    private static DirectivesDispatcher singleton = null;
    /**
     * Map for directives types mappings.
     */
    private static Map<String, Map<String, String>> directivesTypesMappings;
    /**
     * Map for directives actions mappings.
     */
    private static Map<String, Map<String, String>> directivesActionsMappings;
    /**
     * Map for directives effects mappings.
     */
    private static Map<String, Map<String, String>> directivesEffectsMappings;
    /**
     * Message handler singleton instance.
     */
    private final MessageHandler messageHandler;
    /**
     * Object mapper for json serialization/deserialization.
     */
    private final ObjectMapper jsonMapper;

    /**
     * Constructor for directives dispatcher, initializing message handler instance reference, json mapper
     * and directives types, actions and effects mappings.
     *
     * @param messageHandler message handler singleton instance
     * @throws DirectivesDispatcherException if there was an error while parsing directives types/actions/effects mappings
     */
    private DirectivesDispatcher(MessageHandler messageHandler) throws DirectivesDispatcherException {
        this.messageHandler = messageHandler;
        this.jsonMapper = new ObjectMapper();
        try {
            InputStream jsonInputStream = DirectivesDispatcher.class.getResourceAsStream(DIRECTIVES_TYPES_MAPPINGS_RESOURCE_LOCATION);
            directivesTypesMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
            });
            jsonInputStream = DirectivesDispatcher.class.getResourceAsStream(DIRECTIVES_ACTIONS_MAPPINGS_RESOURCE_LOCATION);
            directivesActionsMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
            });
            jsonInputStream = DirectivesDispatcher.class.getResourceAsStream(DIRECTIVES_EFFECTS_MAPPINGS_RESOURCE_LOCATION);
            directivesEffectsMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new DirectivesDispatcherException(
                    String.format("Error while parsing directives types/actions/effects mappings (%s)", e.getMessage()), e);
        }
    }

    /**
     * Get directives dispatcher singleton instance.
     *
     * @param messageHandler message handler singleton instance
     * @return directives dispatcher singleton instance
     * @throws DirectivesDispatcherException if there was an error while creating directives dispatcher
     */
    public static DirectivesDispatcher getInstance(MessageHandler messageHandler) throws DirectivesDispatcherException {
        if (singleton == null)
            singleton = new DirectivesDispatcher(messageHandler);
        return singleton;
    }

    /**
     * Exception for errors within directives dispatcher class.
     */
    public static class DirectivesDispatcherException extends Exception {
        /**
         * DirectivesDispatcherException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public DirectivesDispatcherException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
