package it.polimi.ingsw.javangers.client.view.cli;

import java.util.Map;

/**
 * Utility class for cli constants.
 */
public class CLIConstants {
    /**
     * ANSI escape code for text color reset.
     */
    public static final String ANSI_RESET = "\u001B[0m";
    /**
     * ANSI escape code for black text.
     */
    public static final String ANSI_BLACK = "\u001B[30m";
    /**
     * ANSI escape code for red text.
     */
    public static final String ANSI_RED = "\u001B[31m";
    /**
     * ANSI escape code for green text.
     */
    public static final String ANSI_GREEN = "\u001B[32m";
    /**
     * ANSI escape code for yellow text.
     */
    public static final String ANSI_YELLOW = "\u001B[33m";
    /**
     * ANSI escape code for blue text.
     */
    public static final String ANSI_BLUE = "\u001B[34m";
    /**
     * ANSI escape code for magenta text.
     */
    public static final String ANSI_MAGENTA = "\u001B[35m";
    /**
     * ANSI escape code for cyan text.
     */
    public static final String ANSI_CYAN = "\u001B[36m";
    /**
     * ANSI escape code for white text.
     */
    public static final String ANSI_WHITE = "\u001B[37m";
    /**
     * ANSI escape code for bright black text.
     */
    public static final String ANSI_BRIGHT_BLACK = "\u001B[30;1m";
    /**
     * ANSI escape code for bright red text.
     */
    public static final String ANSI_BRIGHT_RED = "\u001B[31;1m";
    /**
     * ANSI escape code for bright green text.
     */
    public static final String ANSI_BRIGHT_GREEN = "\u001B[32;1m";
    /**
     * ANSI escape code for bright yellow text.
     */
    public static final String ANSI_BRIGHT_YELLOW = "\u001B[33;1m";
    /**
     * ANSI escape code for bright blue text.
     */
    public static final String ANSI_BRIGHT_BLUE = "\u001B[34;1m";
    /**
     * ANSI escape code for bright magenta text.
     */
    public static final String ANSI_BRIGHT_MAGENTA = "\u001B[35;1m";
    /**
     * ANSI escape code for bright cyan text.
     */
    public static final String ANSI_BRIGHT_CYAN = "\u001B[36;1m";
    /**
     * ANSI escape code for bright white text.
     */
    public static final String ANSI_BRIGHT_WHITE = "\u001B[37;1m";
    /**
     * Eriantys cli title with special characters.
     */
    public static final String ERIANTYS_TITLE = """
               ▄████████    ▄████████  ▄█     ▄████████ ███▄▄▄▄       ███     ▄██   ▄      ▄████████\s
              ███    ███   ███    ███ ███    ███    ███ ███▀▀▀██▄ ▀█████████▄ ███   ██▄   ███    ███\s
              ███    █▀    ███    ███ ███▌   ███    ███ ███   ███    ▀███▀▀██ ███▄▄▄███   ███    █▀ \s
             ▄███▄▄▄      ▄███▄▄▄▄██▀ ███▌   ███    ███ ███   ███     ███   ▀ ▀▀▀▀▀▀███   ███       \s
            ▀▀███▀▀▀     ▀▀███▀▀▀▀▀   ███▌ ▀███████████ ███   ███     ███     ▄██   ███ ▀███████████\s
              ███    █▄  ▀███████████ ███    ███    ███ ███   ███     ███     ███   ███          ███\s
              ███    ███   ███    ███ ███    ███    ███ ███   ███     ███     ███   ███    ▄█    ███\s
              ██████████   ███    ███ █▀     ███    █▀   ▀█   █▀     ▄████▀    ▀█████▀   ▄████████▀ \s
                           ███    ███                                                               \s""";
    /**
     * Mappings for wizard types' cli colors.
     */
    protected static final Map<String, String> WIZARD_TYPES_CLI_COLORS = Map.of(
            "d", CLIConstants.ANSI_BRIGHT_GREEN,
            "k", CLIConstants.ANSI_BRIGHT_YELLOW,
            "w", CLIConstants.ANSI_BRIGHT_MAGENTA,
            "s", CLIConstants.ANSI_BRIGHT_CYAN);

    /**
     * Mappings for token colors' cli colors.
     */
    protected static final Map<String, String> TOKEN_COLORS_CLI_COLORS = Map.of(
            "y", CLIConstants.ANSI_BRIGHT_YELLOW,
            "b", CLIConstants.ANSI_BRIGHT_BLUE,
            "g", CLIConstants.ANSI_BRIGHT_GREEN,
            "r", CLIConstants.ANSI_BRIGHT_RED,
            "p", CLIConstants.ANSI_BRIGHT_MAGENTA);

    /**
     * Private constructor for utility class.
     */
    private CLIConstants() {
    }
}
