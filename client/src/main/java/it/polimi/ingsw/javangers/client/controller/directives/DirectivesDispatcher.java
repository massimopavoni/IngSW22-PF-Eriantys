package it.polimi.ingsw.javangers.client.controller.directives;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.client.controller.MessageHandler;

/**
 * Class representing the directives dispatcher.
 */
public class DirectivesDispatcher {
    /**
     * Directives dispatcher singleton instance.
     */
    private static DirectivesDispatcher singleton = null;
    /**
     * Message handler singleton instance.
     */
    private final MessageHandler messageHandler;
    /**
     * Object mapper for json serialization/deserialization.
     */
    private final ObjectMapper jsonMapper;

    /**
     * Constructor for directives dispatcher, initializing message handler instance reference and json mapper.
     *
     * @param messageHandler message handler singleton instance
     */
    private DirectivesDispatcher(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.jsonMapper = new ObjectMapper();
    }

    /**
     * Get directives dispatcher singleton instance.
     *
     * @param messageHandler message handler singleton instance
     * @return directives dispatcher singleton instance
     */
    public static DirectivesDispatcher getInstance(MessageHandler messageHandler) {
        if (singleton == null)
            singleton = new DirectivesDispatcher(messageHandler);
        return singleton;
    }
}
