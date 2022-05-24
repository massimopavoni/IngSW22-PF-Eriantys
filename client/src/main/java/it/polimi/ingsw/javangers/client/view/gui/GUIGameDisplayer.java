package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import it.polimi.ingsw.javangers.client.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing the secondary gui view controller for JavaFX application.
 */
public class GUIGameDisplayer {
    /**
     * Map for token colors labels selectors.
     */
    private static final Map<String, String> TOKEN_COLORS_LABELS =
            View.AVAILABLE_TOKEN_COLORS.values().stream().collect(Collectors.toMap(
                    (String color) -> color.split("_")[0].toLowerCase(), color -> color));
    /**
     * Map for methods corresponding to character cards effects.
     */
    private static final Map<String, Method> EFFECT_METHOD_MAPPINGS;
    /**
     * Tokens list fxml resource literal.
     */
    private static final String TOKENS_LIST_FXML = "tokensList.fxml";
    /**
     * Island frame selector literal.
     */
    private static final String ISLAND_FRAME_SELECTOR = "#islandFrame%d";
    /**
     * Card frame selector literal.
     */
    private static final String CARD_FRAME_SELECTOR = "#%sFrame";

    static {
        Map<String, Method> effectMethodMappings = new HashMap<>();
        List<String> availableEffects = Arrays.asList(
                "monk", "innkeeper", "herald", "mailman", "herbalist", "centaur",
                "jester", "knight", "mushroomer", "bard", "queen", "scoundrel");
        for (String effect : availableEffects) {
            try {
                effectMethodMappings.put(effect,
                        GUIGameDisplayer.class.getDeclaredMethod(effect));
            } catch (NoSuchMethodException e) {
                System.err.printf("Error while creating action/effect method mappings (%s)", e.getMessage());
                System.exit(1);
            }
        }
        EFFECT_METHOD_MAPPINGS = Collections.unmodifiableMap(effectMethodMappings);
    }

    /**
     * Client directives dispatcher instance.
     */
    private final DirectivesDispatcher directivesDispatcher;
    /**
     * Client directives parser instance.
     */
    private final DirectivesParser directivesParser;
    /**
     * Cranio logo eagerly loaded image.
     */
    private final Image cranioLogo;
    /**
     * Map for tower eagerly loaded images.
     */
    private final Map<String, Image> towersImages;
    /**
     * Array for island frame eagerly loaded images.
     */
    private final Image[] islandFramesImages;
    /**
     * Array for island eagerly loaded images.
     */
    private final Image[] islandsImages;
    /**
     * Map for assistant card eagerly loaded images.
     */
    private final Map<String, Image> assistantCardsImages;
    /**
     * Map for character card eagerly loaded images.
     */
    private final Map<String, Image> characterCardsImages;
    /**
     * Alert dialog for errors.
     */
    private final Alert errorAlert;
    /**
     * Alert dialog for messages.
     */
    private final Alert messageAlert;
    /**
     * Main application stage.
     */
    private final Stage stage;
    /**
     * Game view scene.
     */
    private Scene scene;
    /**
     * Popup window stage.
     */
    private Stage popUpStage;
    /**
     * Client player username.
     */
    private String username;
    /**
     * First game show display flag.
     */
    private boolean firstDisplay;
    /**
     * Dashboard ordered usernames list.
     */
    private List<String> usernamesList;
    /**
     * Move students hall flag.
     */
    private boolean moveStudentsToHall;
    /**
     * Activated character card events flag.
     */
    private boolean activatedCharacterCard;
    /**
     * Archipelago size of previous show.
     */
    private int previousArchipelagoSize;
    /**
     * Currently enlightened island index.
     */
    private int enlightenedIsland;
    /**
     * Currently chosen assistant card.
     */
    private String chosenAssistantCard;
    /**
     * Currently chosen character card.
     */
    private String chosenCharacterCard;
    /**
     * Temporary tokens list for multiple lists operations.
     */
    private List<String> tokensList;
    /**
     * Temporary tokens islands tokens map for move students.
     */
    private Map<Integer, List<String>> tokensMap;
    /**
     * Current phase label.
     */
    @FXML
    private Label currentPhase;
    /**
     * Players order label.
     */
    @FXML
    private Label playersOrder;
    /**
     * Main dashboard username label.
     */
    @FXML
    private Label dashboardUsernameMain;
    /**
     * Opposite dashboard username label.
     */
    @FXML
    private Label dashboardUsernameOpposite;
    /**
     * Side dashboard username label.
     */
    @FXML
    private Label dashboardUsernameSide;
    /**
     * Main dashboard token image view.
     */
    @FXML
    private ImageView coinViewMain;
    /**
     * Opposite dashboard token image view.
     */
    @FXML
    private ImageView coinViewOpposite;
    /**
     * Side dashboard token image view.
     */
    @FXML
    private ImageView coinViewSide;
    /**
     * Third dashboard image view.
     */
    @FXML
    private ImageView thirdDashboard;
    /**
     * Third dashboard last discarded assistant card cloud.
     */
    @FXML
    private ImageView thirdDiscardCloud;
    /**
     * Your turn label above buttons.
     */
    @FXML
    private Label yourTurn;
    /**
     * Tokens list label for instructions.
     */
    @FXML
    private Label tokensListLabel;
    /**
     * Fill clouds button.
     */
    @FXML
    private Button fillCloudsButton;
    /**
     * Play assistant card button.
     */
    @FXML
    private Button playAssistantCardButton;
    /**
     * Move students button.
     */
    @FXML
    private Button moveStudentsButton;
    /**
     * Move mother nature button.
     */
    @FXML
    private Button moveMotherNatureButton;
    /**
     * Choose cloud button.
     */
    @FXML
    private Button chooseCloudButton;
    /**
     * Activate character card button.
     */
    @FXML
    private Button activateCharacterCardButton;
    /**
     * Assistant cards grid pane.
     */
    @FXML
    private GridPane assistantCardsGrid;
    /**
     * Character cards grid pane.
     */
    @FXML
    private GridPane characterCardsGrid;
    /**
     * Island info tower image view.
     */
    @FXML
    private ImageView islandTowerView;
    /**
     * Island info tower label.
     */
    @FXML
    private Label islandTowersLabel;
    /**
     * Island info inhibition token image view.
     */
    @FXML
    private ImageView inhibitionTokenView;
    /**
     * Island info inhibition token label.
     */
    @FXML
    private Label inhibitionTokenLabel;

    /**
     * Constructor for main game view controller, initializing directives parser and dispatcher, application stage, alerts,
     * boolean flags and eagerly loaded logo, islands and island frames images.
     *
     * @param directivesParser     directives parser instance
     * @param directivesDispatcher directives dispatcher instance
     * @param stage                application stage
     */
    protected GUIGameDisplayer(DirectivesParser directivesParser, DirectivesDispatcher directivesDispatcher, Stage stage) {
        this.directivesParser = directivesParser;
        this.directivesDispatcher = directivesDispatcher;
        this.stage = stage;
        this.errorAlert = new Alert(Alert.AlertType.ERROR);
        this.messageAlert = new Alert(Alert.AlertType.INFORMATION);
        this.activatedCharacterCard = false;
        this.cranioLogo = new Image(String.valueOf(GUIGameDisplayer.class.getResource("images/cranioLogo.png")));
        this.islandFramesImages = new Image[3];
        this.islandsImages = new Image[4];
        for (int i = 0; i < 3; i++) {
            this.islandFramesImages[i] = new Image(String.valueOf(
                    GUIGameDisplayer.class.getResource(String.format("images/islands/islandFrame%d.png", i))));
            this.islandsImages[i] = new Image(String.valueOf(
                    GUIGameDisplayer.class.getResource(String.format("images/islands/island%d.png", i))));
        }
        this.islandsImages[3] = new Image(String.valueOf(
                GUIGameDisplayer.class.getResource("images/islands/islandMotherNature.png")));
        this.towersImages = new HashMap<>();
        this.assistantCardsImages = new HashMap<>();
        this.characterCardsImages = new HashMap<>();
    }

    /**
     * Open main game view window scene.
     */
    protected void open() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("gameView.fxml"));
            fxmlLoader.setController(this);
            this.scene = new Scene(fxmlLoader.load());
            this.stage.setScene(this.scene);
            this.stage.sizeToScene();
            this.stage.hide();
            this.stage.show();
            this.firstDisplay = true;
        } catch (IOException e) {
            throw new View.ViewException(e.getMessage(), e);
        }
    }

    private void showErrorAlert(String header, String content) {
        this.errorAlert.setHeaderText(header);
        this.errorAlert.setContentText(content);
        this.errorAlert.showAndWait();
    }

    private void showMessageAlert(String header, String content) {
        this.messageAlert.setHeaderText(header);
        this.messageAlert.setContentText(content);
        this.messageAlert.showAndWait();
    }

    public void setYourTurnMessage(String message) {
        this.yourTurn.setText(message);
    }

    public void openPopUp(String fxmlFile) {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource(fxmlFile));
        fxmlLoader.setController(this);
        this.popUpStage = new Stage();
        try {
            this.popUpStage.setScene(new Scene(fxmlLoader.load()));
            this.popUpStage.initModality(Modality.APPLICATION_MODAL);
            this.popUpStage.getIcons().add(this.cranioLogo);
            this.popUpStage.sizeToScene();
            this.popUpStage.setResizable(false);
            this.popUpStage.setOnCloseRequest(event -> this.activatedCharacterCard = false);
            this.popUpStage.show();
        } catch (IOException e) {
            throw new View.ViewException(e.getMessage(), e);
        }
    }

    private void displayCurrentPhase() {
        Pair<String, String> currentPhasePair = this.directivesParser.getCurrentPhase();
        this.currentPhase.setText(String.format("Current phase: %s => %s",
                currentPhasePair.getKey(), currentPhasePair.getValue()));
    }

    private void displayPlayersOrder() throws DirectivesParser.DirectivesParserException {
        this.playersOrder.setText(String.format("Player's order: %s",
                String.join(", ", this.directivesParser.getPlayersOrder())));
    }

    private void displayArchipelago() {
        int archipelagoSize = this.directivesParser.getIslandsSize();
        if (archipelagoSize < this.previousArchipelagoSize) {
            for (int i = archipelagoSize; i < this.previousArchipelagoSize; i++) {
                this.scene.lookup(String.format("#island%d", i)).setVisible(false);
                this.scene.lookup(String.format(ISLAND_FRAME_SELECTOR, i)).setVisible(false);
            }
            this.previousArchipelagoSize = archipelagoSize;
            if (this.enlightenedIsland >= archipelagoSize)
                this.updateEnlightenedIslandInfo(this.directivesParser.getMotherNaturePosition());
        }
        double deltaRad = 2 * Math.PI / archipelagoSize;
        double currentRad = 0;
        ImageView currentIsland;
        ImageView currentFrameIsland;
        for (int i = 0; i < archipelagoSize; i++) {
            currentIsland = (ImageView) this.scene.lookup(String.format("#island%d", i));
            currentFrameIsland = (ImageView) this.scene.lookup(String.format(ISLAND_FRAME_SELECTOR, i));
            currentIsland.setLayoutX(595 + 260 * Math.cos(currentRad));
            currentIsland.setLayoutY(280 + 130 * Math.sin(currentRad));
            currentFrameIsland.setLayoutX(595 + 260 * Math.cos(currentRad));
            currentFrameIsland.setLayoutY(280 + 130 * Math.sin(currentRad));
            currentRad += deltaRad;
            if (i == this.directivesParser.getMotherNaturePosition()) {
                currentFrameIsland.setImage(this.islandFramesImages[0]);
                currentIsland.setImage(this.islandsImages[3]);
            } else {
                currentFrameIsland.setImage(this.islandFramesImages[i % 3]);
                currentIsland.setImage(this.islandsImages[i % 3]);
            }
        }
    }

    private void displayDashboardEntranceTokens() {
        Map<String, Integer> entranceTokens;
        for (int i = 0; i < this.directivesParser.getExactPlayersNumber(); i++) {
            try {
                entranceTokens = this.directivesParser.getDashboardEntranceTokens(this.usernamesList.get(i));
                this.displayTokensLabels(String.format("DashboardEntranceLabel%d", i), entranceTokens);
            } catch (DirectivesParser.DirectivesParserException e) {
                throw new View.ViewException(e.getMessage(), e);
            }
        }
    }

    private void displayDashboardHallTokens() {
        Map<String, Integer> hallTokens;
        for (int i = 0; i < this.directivesParser.getExactPlayersNumber(); i++) {
            try {
                hallTokens = this.directivesParser.getDashboardHallTokens(this.usernamesList.get(i));
                this.displayTokensLabels(String.format("DashboardHallLabel%d", i), hallTokens);
            } catch (DirectivesParser.DirectivesParserException e) {
                throw new View.ViewException(e.getMessage(), e);
            }
        }
    }

    private void handleCharacterCardSelectIsland(int selectedIsland) {
        switch (this.chosenCharacterCard) {
            case "monk" -> {
                this.updateEnlightenedIslandInfo(selectedIsland);
                this.openPopUp(TOKENS_LIST_FXML);
                int multipurposeCounter = this.directivesParser
                        .getCharacterCardMultipurposeCounter(this.chosenCharacterCard);
                this.tokensListLabel.setText(String.format(
                        "Choose up to %d student%s to move from the monk card to the selected island.",
                        multipurposeCounter, multipurposeCounter == 1 ? "" : "s"));
                this.spinnersInit();
            }
            case "herald" -> {
                this.directivesDispatcher.activateHerald(this.username, selectedIsland);
                this.activatedCharacterCard = false;
            }
            case "herbalist" -> {
                this.directivesDispatcher.activateHerbalist(this.username, selectedIsland);
                this.activatedCharacterCard = false;
            }
            default -> {
                // Do nothing for other character cards
            }
        }
    }

    private void handlePhaseSelectIsland(int selectedIsland) {
        switch (this.directivesParser.getCurrentPhase().getValue()) {
            case "Move students" -> {
                this.updateEnlightenedIslandInfo(selectedIsland);
                this.openPopUp(TOKENS_LIST_FXML);
                if (this.tokensMap != null)
                    this.tokensMap.put(this.enlightenedIsland, Collections.emptyList());
                int permittedStudentsNumber = Math.max(0, this.directivesParser.getStudentsPerCloud()
                        - (this.tokensList == null ? 0 : this.tokensList.size())
                        - (this.tokensMap == null ? 0 : this.tokensMap.values().stream().mapToInt(List::size).sum()));
                this.tokensListLabel.setText(String.format(
                        "Choose up to %d student%s to move from your entrance to the selected island.",
                        permittedStudentsNumber, permittedStudentsNumber == 1 ? "" : "s"));
                this.spinnersInit();
                this.moveStudentsToHall = false;
            }
            case "Move mother nature" -> {
                int steps = (selectedIsland - this.directivesParser.getMotherNaturePosition()
                        + this.directivesParser.getIslandsSize()) % this.directivesParser.getIslandsSize();
                if (steps < 1 || steps > this.directivesParser
                        .getDashboardLastDiscardedAssistantCard(this.username).getValue().getValue()
                        + this.directivesParser.getAdditionalMotherNatureSteps()) {
                    this.showErrorAlert("Move mother nature", "Invalid number of steps.");
                } else {
                    this.directivesDispatcher.actionMoveMotherNature(this.username, steps);
                }
            }
            default -> {
                // Do nothing for other phases
            }
        }
    }

    @FXML
    private void selectIsland(MouseEvent mouseEvent) {
        int selectedIsland = Integer.parseInt(((ImageView) mouseEvent.getSource()).getId().split("island")[1]);
        if (this.activatedCharacterCard) {
            this.handleCharacterCardSelectIsland(selectedIsland);
        } else {
            this.handlePhaseSelectIsland(selectedIsland);
        }
    }

    private void displayCloudsTokensLabels() {
        Map<String, Integer> cloudTokens;
        for (int i = 0; i < this.directivesParser.getExactPlayersNumber(); i++) {
            try {
                cloudTokens = this.directivesParser.getCloudTokens(i);
                this.displayTokensLabels(String.format("CloudLabel%d", i), cloudTokens);
            } catch (DirectivesParser.DirectivesParserException e) {
                throw new View.ViewException(e.getMessage(), e);
            }
        }
    }

    @FXML
    private void selectEnlightenedIsland(MouseEvent event) {
        if (((ImageView) event.getSource()).getId().equals("leftArrow"))
            this.updateEnlightenedIslandInfo((this.enlightenedIsland +
                    this.directivesParser.getIslandsSize() - 1) % this.directivesParser.getIslandsSize());
        else
            this.updateEnlightenedIslandInfo((this.enlightenedIsland + 1)
                    % this.directivesParser.getIslandsSize());
    }

    private void updateEnlightenedIslandInfo(int newPosition) {
        ImageView updateFrameIsland = (ImageView) this.scene.lookup(String.format(ISLAND_FRAME_SELECTOR, this.enlightenedIsland));
        updateFrameIsland.setVisible(false);
        this.enlightenedIsland = newPosition;
        updateFrameIsland = (ImageView) this.scene.lookup(String.format(ISLAND_FRAME_SELECTOR, this.enlightenedIsland));
        updateFrameIsland.setVisible(true);
        if (this.directivesParser.isExpertMode()) {
            if (this.firstDisplay)
                this.inhibitionTokenView.setVisible(true);
            else
                this.inhibitionTokenLabel.setText(String.valueOf(
                        this.directivesParser.getIslandEnabled(this.enlightenedIsland)));
        }
        try {
            Map<String, Integer> islandTokens = this.directivesParser.getIslandTokens(this.enlightenedIsland);
            this.displayTokensLabels("IslandLabel", islandTokens);
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new View.ViewException(e.getMessage(), e);
        }
        Pair<String, Integer> islandTowers = this.directivesParser.getIslandTowers(this.enlightenedIsland);
        if (islandTowers.getValue() != 0) {
            this.islandTowerView.setImage(this.towersImages.get(islandTowers.getKey()));
            this.islandTowerView.setVisible(true);
            this.islandTowersLabel.setText(islandTowers.getValue().toString());
        } else {
            this.islandTowerView.setVisible(false);
            this.islandTowersLabel.setText("");
        }
    }

    private void displayTokensLabels(String selector, Map<String, Integer> tokens) {
        GUIGameDisplayer.TOKEN_COLORS_LABELS
                .forEach((label, color) -> ((Label) this.scene.lookup(String.format("#%s%s", label, selector))).setText(
                        tokens.get(color) != null ? tokens.get(color).toString() : "0"));
    }

    private void displayCoinsLabels() {
        for (int i = 0; i < this.directivesParser.getExactPlayersNumber(); i++)
            ((Label) this.scene.lookup(String.format("#coinLabel%d", i)))
                    .setText(String.format("%d", this.directivesParser.getDashboardCoins(this.usernamesList.get(i))));
    }

    protected void displayGame(String username) throws DirectivesParser.DirectivesParserException {
        if (this.firstDisplay) {
            this.username = username;
            View.AVAILABLE_TOWER_COLORS.values().forEach(color -> this.towersImages.put(color,
                    new Image(String.valueOf(GUIGameDisplayer.class.getResource(
                            String.format("images/towers/%sTower.png", color.toLowerCase()))))));
            this.directivesParser.getDashboardAssistantCards(this.username).keySet()
                    .forEach(card -> this.assistantCardsImages.put(card, new Image(String.valueOf(
                            GUIGameDisplayer.class.getResource(String.format("images/assistantCards/%s.png", card))))));
            this.directivesParser.getCharacterCardNames().forEach(card -> this.characterCardsImages.put(card, new Image(
                    String.valueOf(GUIGameDisplayer.class.getResource(String.format("images/characterCards/%s.png", card))))));
            this.usernamesList = directivesParser.getDashboardUsernames();
            this.usernamesList.remove(this.username);
            this.usernamesList.add(0, this.username);
            this.dashboardUsernameMain.setText(this.username);
            this.dashboardUsernameOpposite.setText(this.usernamesList.get(1));
            boolean threePlayers = this.directivesParser.getExactPlayersNumber() == 3;
            if (threePlayers) {
                this.thirdDashboard.setVisible(true);
                this.thirdDiscardCloud.setVisible(true);
                this.dashboardUsernameSide.setText(this.usernamesList.get(2));
                this.scene.lookup("#cloud2").setVisible(true);
            }
            if (directivesParser.isExpertMode()) {
                activateCharacterCardButton.setVisible(true);
                this.coinViewMain.setVisible(true);
                this.coinViewOpposite.setVisible(true);
                if (threePlayers)
                    this.coinViewSide.setVisible(true);
            }
            this.previousArchipelagoSize = this.directivesParser.getIslandsSize();
            this.updateEnlightenedIslandInfo(this.directivesParser.getMotherNaturePosition());
        }
        this.displayCurrentPhase();
        this.displayPlayersOrder();
        ((Label) this.scene.lookup("#studentsBagLabel")).setText(String.valueOf(
                this.directivesParser.getStudentsBagTokens().values().stream().mapToInt(Integer::intValue).sum()));
        this.updateEnlightenedIslandInfo(this.enlightenedIsland);
        this.displayArchipelago();
        this.displayCloudsTokensLabels();
        this.displayDashboardTowers();
        this.displayDashboardEntranceTokens();
        this.displayDashboardHallTokens();
        this.displayDashboardTeachers();
        this.displayLastDiscardedCards();
        if (directivesParser.isExpertMode())
            this.displayCoinsLabels();
        if (this.firstDisplay)
            this.firstDisplay = false;
    }

    private void displayDashboardTeachers() {
        try {
            Map<String, String> teachers = this.directivesParser.getTeachers();
            ImageView teacherView;
            for (int i = 0; i < this.directivesParser.getExactPlayersNumber(); i++) {
                for (Map.Entry<String, String> label : GUIGameDisplayer.TOKEN_COLORS_LABELS.entrySet()) {
                    teacherView = (ImageView) this.scene.lookup(
                            String.format("#%sDashboardTeacher%d", label.getKey(), i));
                    if (teachers.get(label.getValue()).equals(this.usernamesList.get(i))) {
                        teacherView.setVisible(true);
                        teacherView = (ImageView) this.scene.lookup(String.format("#%sTeacher", label.getKey()));
                        if (teacherView.isVisible())
                            teacherView.setVisible(false);
                    } else
                        teacherView.setVisible(false);
                }
            }
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new View.ViewException(e.getMessage(), e);
        }
    }

    private void displayLastDiscardedCards() {
        for (int i = 0; i < this.directivesParser.getExactPlayersNumber(); i++) {
            Map.Entry<String, Pair<Integer, Integer>> lastDiscard =
                    this.directivesParser.getDashboardLastDiscardedAssistantCard(this.usernamesList.get(i));
            if (lastDiscard != null) {
                if (this.directivesParser.getDashboardDiscardedAssistantCards(this.usernamesList.get(i)).size() == 1)
                    this.scene.lookup(String.format("#lastDiscardShadow%d", i)).setVisible(true);
                ((ImageView) this.scene.lookup(String.format("#lastDiscard%d", i))).setImage(
                        this.assistantCardsImages.get(lastDiscard.getKey()));
            }
        }
    }

    private void displayDashboardTowers() {
        for (int i = 0; i < this.directivesParser.getExactPlayersNumber(); i++) {
            if (this.firstDisplay) {
                ImageView towerView = (ImageView) this.scene.lookup(String.format("#tower%d", i));
                towerView.setImage(this.towersImages.get(
                        this.directivesParser.getDashboardTowers(this.usernamesList.get(i)).getKey()));
            }
            ((Label) this.scene.lookup("#towerLabel" + i)).setText(
                    this.directivesParser.getDashboardTowers(this.usernamesList.get(i)).getValue().toString());
        }
    }

    private void displayAvailableAssistantCards() {
        this.chosenAssistantCard = null;
        List<String> cardNamesList = new ArrayList<>(
                this.directivesParser.getDashboardAssistantCards(this.username).keySet());
        ImageView assistantCardView;
        for (int i = 0; i < cardNamesList.size(); i++) {
            assistantCardView = (ImageView) this.assistantCardsGrid.getChildren().get(i * 2);
            assistantCardView.setId(cardNamesList.get(i));
            assistantCardView.setImage(this.assistantCardsImages.get(cardNamesList.get(i)));
            this.assistantCardsGrid.getChildren().get(i * 2 + 1).setId(String.format("%sFrame", cardNamesList.get(i)));
        }
        for (int i = cardNamesList.size() * 2; i < this.assistantCardsGrid.getChildren().size(); i += 2)
            this.assistantCardsGrid.getChildren().get(i).setVisible(false);
    }

    private void displayAvailableCharacterCards() {
        this.chosenCharacterCard = null;
        List<String> cardNamesList = this.directivesParser.getCharacterCardNames();
        ImageView characterCardView;
        for (int i = 0; i < cardNamesList.size(); i++) {
            characterCardView = (ImageView) this.characterCardsGrid.getChildren().get(i * 2);
            characterCardView.setId(cardNamesList.get(i));
            characterCardView.setImage(this.characterCardsImages.get(cardNamesList.get(i)));
            this.characterCardsGrid.getChildren().get(i * 2 + 1).setId(String.format("%sFrame", cardNamesList.get(i)));
        }
    }

    @FXML
    private void selectAssistantCard(MouseEvent event) {
        if (this.chosenAssistantCard != null)
            this.assistantCardsGrid.lookup(
                    String.format(CARD_FRAME_SELECTOR, this.chosenAssistantCard)).setVisible(false);
        this.chosenAssistantCard = ((ImageView) event.getSource()).getId();
        this.assistantCardsGrid.lookup(
                String.format(CARD_FRAME_SELECTOR, this.chosenAssistantCard)).setVisible(true);
    }

    private void displayCharacterCardInfo() {
        Pair<Integer, Integer> cardCost = this.directivesParser.getCharacterCardCost(this.chosenCharacterCard);
        ((Label) this.popUpStage.getScene().lookup("#cardCostLabel"))
                .setText(String.format("%d + %d", cardCost.getKey(), cardCost.getValue()));
        try {
            Map<String, Integer> cardTokens = this.directivesParser.getCharacterCardTokens(this.chosenCharacterCard);
            GUIGameDisplayer.TOKEN_COLORS_LABELS
                    .forEach((label, color) -> ((Label) this.popUpStage.getScene().lookup(
                            String.format("#%sCardLabel", label))).setText(
                            cardTokens.get(color) != null ? cardTokens.get(color).toString() : "0"));
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new View.ViewException(e.getMessage(), e);
        }
        ((Label) this.popUpStage.getScene().lookup("#counterLabel")).setText(
                String.format("%d", this.directivesParser.getCharacterCardMultipurposeCounter(this.chosenCharacterCard)));
    }

    @FXML
    private void selectCharacterCard(MouseEvent event) {
        if (this.chosenCharacterCard != null)
            this.characterCardsGrid.lookup(
                    String.format(CARD_FRAME_SELECTOR, this.chosenCharacterCard)).setVisible(false);
        this.chosenCharacterCard = ((ImageView) event.getSource()).getId();
        this.characterCardsGrid.lookup(
                String.format(CARD_FRAME_SELECTOR, this.chosenCharacterCard)).setVisible(true);
        this.displayCharacterCardInfo();
    }

    protected void displayEndgame(List<String> winnersList) {
        this.openPopUp(winnersList.contains(this.username) ? "winningEndgame.fxml" : "losingEndgame.fxml");
        ((Label) this.popUpStage.getScene().lookup("#winnersLabel"))
                .setText(String.format("Winners: %s", String.join(", ", winnersList)));
        ((Label) this.popUpStage.getScene().lookup("#endgameLabel"))
                .setText(View.POSSIBLE_ENDGAMES.get(this.directivesParser.getEndGame()));
        this.popUpStage.hide();
        this.popUpStage.showAndWait();
    }

    @FXML
    private void fillClouds() {
        this.directivesDispatcher.actionFillClouds(this.username);
    }

    @FXML
    private void playAssistantCard() {
        this.openPopUp("assistantCardsChoice.fxml");
        this.displayAvailableAssistantCards();
    }

    @FXML
    private void activateCharacterCard() {
        this.openPopUp("characterCardsChoice.fxml");
        this.displayAvailableCharacterCards();
    }

    @FXML
    private void confirmAssistantCard() {
        this.popUpStage.close();
        this.directivesDispatcher.actionPlayAssistantCard(this.username, this.chosenAssistantCard);
    }

    @FXML
    private void moveStudentsToHall() {
        if (this.directivesParser.getCurrentPhase().getValue().equals("Move students")) {
            this.tokensList = null;
            this.openPopUp(TOKENS_LIST_FXML);
            int allowedStudentsNumber = Math.max(0, this.directivesParser.getStudentsPerCloud()
                    - (this.tokensList == null ? 0 : this.tokensList.size())
                    - (this.tokensMap == null ? 0 : this.tokensMap.values().stream().mapToInt(List::size).sum()));
            this.tokensListLabel.setText(String.format(
                    "Choose up to %d student%s to move from your entrance to your hall.",
                    allowedStudentsNumber, allowedStudentsNumber == 1 ? "" : "s"));
            this.spinnersInit();
            this.moveStudentsToHall = true;
        }
    }

    @FXML
    private void moveStudents() {
        this.showMessageAlert("Move students", String.format(
                "You must move exactly %s students from your entrance to your hall or/and on some islands.",
                this.directivesParser.getStudentsPerCloud()));
        this.activatedCharacterCard = false;
        this.tokensMap = null;
    }

    private void spinnersInit() {
        for (String tokenColor : View.AVAILABLE_TOKEN_COLORS.values().stream()
                .map(color -> color.split("_")[0].toLowerCase()).toList()) {
            SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 14000605);
            spinnerValueFactory.setValue(0);
            Spinner<Integer> spinner = (Spinner<Integer>) this.popUpStage.getScene().lookup(String.format("#%sSpinner", tokenColor));
            spinner.setValueFactory(spinnerValueFactory);
        }
    }

    @FXML
    private void tokensSpinnersScroll(ScrollEvent scrollEvent) {
        Spinner<Integer> spinner = (Spinner<Integer>) scrollEvent.getSource();
        if (scrollEvent.getDeltaY() > 0) {
            spinner.increment();
        } else if (scrollEvent.getDeltaY() < 0) {
            spinner.decrement();
        }
    }

    private void handleCharacterCardConfirmTokensList(List<String> tokens) {
        int multipurposeCounter = this.directivesParser.getCharacterCardMultipurposeCounter(this.chosenCharacterCard);
        switch (this.chosenCharacterCard) {
            case "monk" -> this.handleMonk(multipurposeCounter, tokens);
            case "jester" -> this.handleJester(multipurposeCounter, tokens);
            case "bard" -> this.handleBard(multipurposeCounter, tokens);
            case "queen" -> this.handleQueen(multipurposeCounter, tokens);
            default ->
                    throw new View.ViewException("Impossible to reach confirm tokens list from chosen character card.");
        }
    }

    private void handleMonk(int multipurposeCounter, List<String> tokens) {
        if (tokens.size() > multipurposeCounter) {
            this.showErrorAlert("Monk", String.format(
                    "You must choose up to %d token%s to move from the monk card to the selected island.",
                    multipurposeCounter, multipurposeCounter == 1 ? "" : "s"));
        } else {
            this.directivesDispatcher.activateMonk(this.username, tokens, this.enlightenedIsland);
        }
        this.activatedCharacterCard = false;
        this.popUpStage.close();
    }

    private void handleJester(int multipurposeCounter, List<String> tokens) {
        if (this.tokensList == null) {
            this.popUpStage.close();
            this.tokensList = tokens;
            this.openPopUp(TOKENS_LIST_FXML);
            this.tokensListLabel.setText(String.format(
                    "Choose exactly %d student%s to move from your entrance to the jester card.",
                    this.tokensList.size(), this.tokensList.size() == 1 ? "" : "s"));
            this.spinnersInit();
        } else {
            if (this.tokensList.size() > multipurposeCounter || this.tokensList.size() != tokens.size()) {
                this.showErrorAlert("Jester", String.format(
                        "You must choose up to %d student%s to move both to and from your entrance.",
                        multipurposeCounter, multipurposeCounter == 1 ? "" : "s"));
            } else {
                this.directivesDispatcher.activateJester(this.username, this.tokensList, tokens);
            }
            this.activatedCharacterCard = false;
            this.tokensList = null;
            this.popUpStage.close();
        }
    }

    private void handleBard(int multipurposeCounter, List<String> tokens) {
        if (this.tokensList == null) {
            this.popUpStage.close();
            this.tokensList = tokens;
            this.openPopUp(TOKENS_LIST_FXML);
            this.tokensListLabel.setText(String.format(
                    "Choose exactly %d student%s to move from your entrance to your hall.",
                    this.tokensList.size(), this.tokensList.size() == 1 ? "" : "s"));
            this.spinnersInit();
        } else {
            if (this.tokensList.size() > multipurposeCounter || this.tokensList.size() != tokens.size()) {
                this.showErrorAlert("Bard", String.format(
                        "You must choose up to %d student%s to move both to and from your hall.",
                        multipurposeCounter, multipurposeCounter == 1 ? "" : "s"));
            } else {
                this.directivesDispatcher.activateBard(this.username, this.tokensList, tokens);
            }
            this.activatedCharacterCard = false;
            this.tokensList = null;
            this.popUpStage.close();
        }
    }

    private void handleQueen(int multipurposeCounter, List<String> tokens) {
        if (tokens.size() > multipurposeCounter) {
            this.showErrorAlert("Queen", String.format(
                    "You must choose up to %d token%s to move from the queen card to your hall.",
                    multipurposeCounter, multipurposeCounter == 1 ? "" : "s"));
        } else {
            this.directivesDispatcher.activateQueen(this.username, tokens);
        }
        this.activatedCharacterCard = false;
        this.popUpStage.close();
    }

    private void handleMoveStudentsConfirmTokensList(List<String> tokens) {
        int studentsPerCloud = this.directivesParser.getStudentsPerCloud();
        if (this.tokensList == null)
            this.tokensList = new ArrayList<>();
        if (this.tokensMap == null)
            this.tokensMap = new HashMap<>();
        if (this.moveStudentsToHall) {
            this.tokensList = tokens;
            int totalTokens = this.tokensList.size() +
                    this.tokensMap.values().stream().mapToInt(List::size).sum();
            if (totalTokens > studentsPerCloud) {
                this.showErrorAlert("Move students", String.format(
                        "You have to move exactly %d students from entrance to hall and/or islands.",
                        studentsPerCloud));
                this.tokensList = null;
                this.tokensMap = null;
            } else if (totalTokens == studentsPerCloud) {
                this.directivesDispatcher.actionMoveStudents(this.username, this.tokensList, this.tokensMap);
                this.tokensList = null;
                this.tokensMap = null;
            }
        } else {
            this.tokensMap.put(this.enlightenedIsland, tokens);
            int totalTokens = this.tokensList.size() +
                    this.tokensMap.values().stream().mapToInt(List::size).sum();
            if (totalTokens > studentsPerCloud) {
                this.showErrorAlert("Move students", String.format(
                        "You have to move exactly %d students from entrance to hall and/or islands.",
                        studentsPerCloud));
                this.tokensList = null;
            } else if (totalTokens == studentsPerCloud) {
                this.directivesDispatcher.actionMoveStudents(this.username, this.tokensList, this.tokensMap);
                this.tokensList = null;
                this.tokensMap = null;
            }
        }
        this.popUpStage.close();
    }

    @FXML
    private void confirmTokensList() {
        ArrayList<String> tokens = new ArrayList<>();
        for (String tokenColor : View.AVAILABLE_TOKEN_COLORS.values()) {
            Spinner<Integer> spinner = (Spinner<Integer>) this.popUpStage.getScene().lookup(
                    String.format("#%sSpinner", tokenColor.split("_")[0].toLowerCase()));
            tokens.addAll(Collections.nCopies(spinner.getValue(), tokenColor));
        }
        if (this.activatedCharacterCard) {
            this.handleCharacterCardConfirmTokensList(tokens);
        } else {
            this.handleMoveStudentsConfirmTokensList(tokens);
        }
    }


    @FXML
    private void moveMotherNature() {
        this.showMessageAlert("Move mother nature",
                String.format("Choose an island by clicking on it (your maximum number of steps for current turn is %d).",
                        this.directivesParser.getDashboardLastDiscardedAssistantCard(this.username).getValue().getValue()
                                + this.directivesParser.getAdditionalMotherNatureSteps()));
        this.activatedCharacterCard = false;
    }

    @FXML
    private void chooseCloud() {
        this.showMessageAlert("Choose cloud", "Choose a cloud by clicking on it.");
        this.activatedCharacterCard = false;
    }

    @FXML
    private void selectCloud(MouseEvent event) {
        if (this.directivesParser.getCurrentPhase().getValue().equals("Choose cloud")) {
            int chosenCloud = Integer.parseInt(((ImageView) event.getSource()).getId().split("cloud")[1]);
            this.directivesDispatcher.actionChooseCloud(this.username, chosenCloud);
        }
    }

    @FXML
    private void confirmCharacterCard() {
        this.popUpStage.close();
        if (this.chosenCharacterCard != null) {
            this.activatedCharacterCard = true;
            try {
                GUIGameDisplayer.EFFECT_METHOD_MAPPINGS.get(this.chosenCharacterCard).invoke(this);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new View.ViewException(e.getMessage(), e);
            }
        }
    }

    @FXML
    private void selectTokenColor(MouseEvent event) {
        String color = View.AVAILABLE_TOKEN_COLORS.get(
                ((ImageView) event.getSource()).getId().substring(0, 1));
        if (this.activatedCharacterCard) {
            switch (chosenCharacterCard) {
                case "mushroomer" -> {
                    this.directivesDispatcher.activateMushroomer(this.username, color);
                    this.activatedCharacterCard = false;
                }
                case "scoundrel" -> {
                    this.directivesDispatcher.activateScoundrel(this.username, color);
                    this.activatedCharacterCard = false;
                }
                default -> {
                    // Do nothing for other character cards
                }
            }
        }
    }

    private void innkeeper() {
        this.directivesDispatcher.activateInnkeeper(this.username);
        this.activatedCharacterCard = false;
    }

    private void mailman() {
        this.directivesDispatcher.activateMailman(this.username);
        this.activatedCharacterCard = false;
    }

    private void centaur() {
        this.directivesDispatcher.activateCentaur(this.username);
        this.activatedCharacterCard = false;
    }

    private void knight() {
        this.directivesDispatcher.activateKnight(this.username);
        this.activatedCharacterCard = false;
    }

    private void herald() {
        this.showMessageAlert("Activate character card: herald",
                "Choose an island by clicking on it.");
    }

    private void herbalist() {
        this.showMessageAlert("Activate character card: herbalist",
                "Choose an island by clicking on it.");
    }

    private void mushroomer() {
        this.showMessageAlert("Activate character card: mushroomer",
                "Choose a color by clicking on island info panel tokens.");

    }

    private void scoundrel() {
        this.showMessageAlert("Activate character card: scoundrel",
                "Choose a color by clicking on island info panel tokens.");
    }

    private void monk() {
        this.showMessageAlert("Activate character card: monk",
                "Choose an island by clicking on it.");
    }

    private void queen() {
        this.openPopUp(TOKENS_LIST_FXML);
        int multipurposeCounter = this.directivesParser.getCharacterCardMultipurposeCounter(this.chosenCharacterCard);
        this.tokensListLabel.setText(String.format(
                "Choose up to %d student%s to move from the queen card to your hall.",
                multipurposeCounter, multipurposeCounter == 1 ? "s" : ""));
        this.spinnersInit();
        this.tokensList = null;
    }

    private void jester() {
        this.openPopUp(TOKENS_LIST_FXML);
        int multipurposeCounter = this.directivesParser.getCharacterCardMultipurposeCounter(this.chosenCharacterCard);
        this.tokensListLabel.setText(String.format(
                "Choose up to %d student%s to move from the jester card to your entrance.",
                multipurposeCounter, multipurposeCounter == 1 ? "s" : ""));
        this.spinnersInit();
        this.tokensList = null;
    }

    private void bard() {
        this.openPopUp(TOKENS_LIST_FXML);
        int multipurposeCounter = this.directivesParser.getCharacterCardMultipurposeCounter(this.chosenCharacterCard);
        this.tokensListLabel.setText(String.format(
                "Choose up to %d student%s to move from your hall to your entrance.",
                multipurposeCounter, multipurposeCounter == 1 ? "s" : ""));
        this.spinnersInit();
        this.tokensList = null;
    }

    protected Button getFillCloudsButton() {
        return this.fillCloudsButton;
    }

    protected Button getPlayAssistantCardButton() {
        return this.playAssistantCardButton;
    }

    protected Button getMoveStudentsButton() {
        return this.moveStudentsButton;
    }

    protected Button getMoveMotherNatureButton() {
        return this.moveMotherNatureButton;
    }

    protected Button getChooseCloudButton() {
        return this.chooseCloudButton;
    }

    protected Button getActivateCharacterCardButton() {
        return this.activateCharacterCardButton;
    }
}