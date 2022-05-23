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


public class GUIGameDisplayer {
    private static final Map<String, String> TOKEN_COLORS_LABELS =
            View.AVAILABLE_TOKEN_COLORS.values().stream().collect(Collectors.toMap(
                    (String color) -> color.split("_")[0].toLowerCase(), color -> color));
    private static final Map<String, Method> EFFECT_METHOD_MAPPINGS;

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

    private final DirectivesParser directivesParser;
    private final DirectivesDispatcher directivesDispatcher;
    private final Image cranioLogo;
    private final Map<String, Image> towersImages;
    private final Image[] islandFramesImages;
    private final Image[] islandsImages;
    private final Map<String, Image> assistantCardsImages;
    private final Map<String, Image> characterCardsImages;
    private final Alert errorAlert;
    private final Alert messageAlert;
    private final Stage stage;
    private Scene scene;
    private Stage popUpStage;
    private String username;
    private boolean firstDisplay;
    private List<String> usernamesList;
    private boolean moveStudentsToHall;
    private boolean activatedCharacterCard;
    private int previousArchipelagoSize;
    private int enlightenedIsland;
    private String chosenAssistantCard;
    private String chosenCharacterCard;
    private List<String> tokensList;
    private Map<Integer, List<String>> tokensMap;
    @FXML
    private Label currentPhase;
    @FXML
    private Label youArePlayerLabel;
    @FXML
    private Label playersOrder;
    @FXML
    private Label dashboardName0;
    @FXML
    private Label dashboardName1;
    @FXML
    private Label dashboardName2;
    @FXML
    private ImageView thirdDashboard;
    @FXML
    private ImageView thirdDiscardCloud;
    @FXML
    private Label yourTurn;
    @FXML
    private Label tokensListLabel;
    @FXML
    private Button fillCloudsButton;
    @FXML
    private Button playAssistantCardButton;
    @FXML
    private Button moveStudentsButton;
    @FXML
    private Button moveMotherNatureButton;
    @FXML
    private Button chooseCloudButton;
    @FXML
    private Button activateCharacterCardButton;
    @FXML
    private GridPane assistantCardsGrid;
    @FXML
    private GridPane characterCardsGrid;
    @FXML
    private ImageView islandTowerView;
    @FXML
    private Label islandTowersLabel;
    @FXML
    private ImageView inhibitionTokenView;
    @FXML
    private Label inhibitionTokenLabel;
    @FXML
    private ImageView coinView0;
    @FXML
    private ImageView coinView1;
    @FXML
    private ImageView coinView2;

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
            throw new RuntimeException(e);
        }
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
            this.popUpStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void closePopUp() {
        if (this.popUpStage != null)
            this.popUpStage.close();
    }

    private void displayCurrentPhase() {
        Pair<String, String> currentPhasePair = this.directivesParser.getCurrentPhase();
        this.currentPhase.setText(String.format("Current phase: %s => %s",
                currentPhasePair.getKey(), currentPhasePair.getValue()));
    }

    private void displayPlayersOrder(String username) throws DirectivesParser.DirectivesParserException {
        this.playersOrder.setText(String.format("Player's order: %s",
                String.join(", ", this.directivesParser.getPlayersOrder())));
    }

    private void displayArchipelago() {
        int archipelagoSize = this.directivesParser.getIslandsSize();
        if (archipelagoSize < this.previousArchipelagoSize) {
            for (int i = archipelagoSize; i < this.previousArchipelagoSize; i++) {
                this.scene.lookup(String.format("#island%d", i)).setVisible(false);
                this.scene.lookup(String.format("#islandFrame%d", i)).setVisible(false);
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
            currentFrameIsland = (ImageView) this.scene.lookup(String.format("#islandFrame%d", i));
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
                e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void selectIsland(MouseEvent mouseEvent) {
        int selectedIsland = Integer.parseInt(((ImageView) mouseEvent.getSource()).getId().split("island")[1]);
        if (this.activatedCharacterCard) {
            switch (this.chosenCharacterCard) {
                case "herald" -> {
                    this.directivesDispatcher.activateHerald(this.username, selectedIsland);
                    this.activatedCharacterCard = false;
                }
                case "herbalist" -> {
                    this.directivesDispatcher.activateHerbalist(this.username, selectedIsland);
                    this.activatedCharacterCard = false;
                }
                case "monk" -> {
                    this.updateEnlightenedIslandInfo(selectedIsland);
                    this.openPopUp("tokensList.fxml");
                    this.tokensListLabel.setText("Choose one student to move from this card to the selected island");
                    this.spinnersInit();
                }
            }
        } else {
            switch (this.directivesParser.getCurrentPhase().getValue()) {
                case "Move mother nature" -> {
                    int steps = (selectedIsland - this.directivesParser.getMotherNaturePosition()
                            + this.directivesParser.getIslandsSize()) % this.directivesParser.getIslandsSize();
                    if (steps < 1 || steps > this.directivesParser
                            .getDashboardLastDiscardedAssistantCard(this.username).getValue().getValue()
                            + this.directivesParser.getAdditionalMotherNatureSteps()) {
                        this.showErrorAlert("Move mother nature", "Invalid number of steps");
                    } else {
                        this.directivesDispatcher.actionMoveMotherNature(this.username, steps);
                    }
                }
                case "Move students" -> {
                    this.updateEnlightenedIslandInfo(selectedIsland);
                    this.openPopUp("tokensList.fxml");
                    this.tokensListLabel.setText("Choose some students to move from your entrance to the selected island");
                    this.spinnersInit();
                    this.moveStudentsToHall = false;
                }
            }
        }
    }

    private void displayCloudsTokensLabels() {
        Map<String, Integer> cloudTokens;
        for (int i = 0; i < this.directivesParser.getExactPlayersNumber(); i++) {
            try {
                cloudTokens = this.directivesParser.getCloudTokens(i);
                this.displayTokensLabels(String.format("CloudLabel%d", i), cloudTokens);
            } catch (DirectivesParser.DirectivesParserException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void selectEnlightenedIsland(MouseEvent event) {
        if (((ImageView) event.getSource()).getId().equals("leftArrow"))
            this.updateEnlightenedIslandInfo((this.enlightenedIsland +
                    this.directivesParser.getIslandsSize() - 1) % this.directivesParser.getIslandsSize());
        else
            this.updateEnlightenedIslandInfo((this.enlightenedIsland +
                    this.directivesParser.getIslandsSize() + 1) % this.directivesParser.getIslandsSize());
    }

    private void updateEnlightenedIslandInfo(int newPosition) {
        if (this.directivesParser.isExpertMode()) {
            if (this.firstDisplay)
                this.inhibitionTokenView.setVisible(true);
            else
                this.inhibitionTokenLabel.setText(String.valueOf(
                        this.directivesParser.getIslandEnabled(this.enlightenedIsland)));
        }
        ImageView updateFrameIsland = (ImageView) this.scene.lookup(String.format("#islandFrame%d", this.enlightenedIsland));
        if (updateFrameIsland != null)
            updateFrameIsland.setVisible(false);
        this.enlightenedIsland = newPosition;
        updateFrameIsland = (ImageView) this.scene.lookup(String.format("#islandFrame%d", this.enlightenedIsland));
        updateFrameIsland.setVisible(true);
        try {
            Map<String, Integer> islandTokens = this.directivesParser.getIslandTokens(this.enlightenedIsland);
            this.displayTokensLabels("IslandLabel", islandTokens);
        } catch (DirectivesParser.DirectivesParserException e) {
            e.printStackTrace();
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
            this.usernamesList = directivesParser.getDashboardNames();
            this.usernamesList.remove(this.username);
            this.usernamesList.add(0, this.username);
            this.dashboardName0.setText(this.username);
            this.dashboardName1.setText(this.usernamesList.get(1));
            boolean threePlayers = this.directivesParser.getExactPlayersNumber() == 3;
            if (threePlayers) {
                this.scene.lookup("#cloud2").setVisible(true);
                this.dashboardName2.setText(this.usernamesList.get(2));
                this.thirdDashboard.setVisible(true);
                this.thirdDiscardCloud.setVisible(true);
            }
            if (directivesParser.isExpertMode()) {
                activateCharacterCardButton.setVisible(true);
                this.coinView0.setVisible(true);
                this.coinView1.setVisible(true);
                if (threePlayers)
                    this.coinView2.setVisible(true);
            }
            this.usernamesList = directivesParser.getDashboardNames();
            this.usernamesList.remove(this.username);
            this.usernamesList.add(0, this.username);
            this.previousArchipelagoSize = this.directivesParser.getIslandsSize();
            this.updateEnlightenedIslandInfo(this.directivesParser.getMotherNaturePosition());
        }
        ((Label) this.scene.lookup("#studentsBagLabel")).setText(String.valueOf(
                this.directivesParser.getStudentsBagTokens().values().stream().mapToInt(Integer::intValue).sum()));
        if (directivesParser.isExpertMode())
            this.displayCoinsLabels();
        this.displayDashboardTowers();
        this.displayCurrentPhase();
        this.displayPlayersOrder(username);
        this.displayArchipelago();
        this.updateEnlightenedIslandInfo(this.enlightenedIsland);
        this.displayDashboardEntranceTokens();
        this.displayDashboardHallTokens();
        this.displayDashboardTeachers();
        this.displayCloudsTokensLabels();
        this.displayLastDiscardedCards();
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
            throw new RuntimeException(e);
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
                    String.format("#%sFrame", this.chosenAssistantCard)).setVisible(false);
        this.chosenAssistantCard = ((ImageView) event.getSource()).getId();
        this.assistantCardsGrid.lookup(
                String.format("#%sFrame", this.chosenAssistantCard)).setVisible(true);
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
            throw new RuntimeException(e);
        }
        ((Label) this.popUpStage.getScene().lookup("#counterLabel")).setText(
                String.format("%d", this.directivesParser.getCharacterCardMultipurposeCounter(this.chosenCharacterCard)));
    }

    @FXML
    private void selectCharacterCard(MouseEvent event) {
        if (this.chosenCharacterCard != null)
            this.characterCardsGrid.lookup(
                    String.format("#%sFrame", this.chosenCharacterCard)).setVisible(false);
        this.chosenCharacterCard = ((ImageView) event.getSource()).getId();
        this.characterCardsGrid.lookup(
                String.format("#%sFrame", this.chosenCharacterCard)).setVisible(true);
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
    private void playCharacterCard() {
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
            this.openPopUp("tokensList.fxml");
            this.tokensListLabel.setText("Choose some students to move from your entrance to your hall");
            this.spinnersInit();
            this.moveStudentsToHall = true;
        }
    }

    @FXML
    private void moveStudents() {
        this.showMessageAlert("Move students", String.format(
                "You must move exactly %s students form your entrance to your hall or on some islands",
                this.directivesParser.getStudentsPerCloud()));
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

    @FXML
    private void tokensListConfirm() {
        ArrayList<String> tokens = new ArrayList<>();
        for (String tokenColor : View.AVAILABLE_TOKEN_COLORS.values()) {
            Spinner<Integer> spinner = (Spinner<Integer>) this.popUpStage.getScene().lookup(
                    String.format("#%sSpinner", tokenColor.split("_")[0].toLowerCase()));
            tokens.addAll(Collections.nCopies(spinner.getValue(), tokenColor));
        }
        if (this.activatedCharacterCard) {
            switch (this.chosenCharacterCard) {
                case "jester" -> {
                    if (this.tokensList == null) {
                        this.popUpStage.close();
                        this.tokensList = tokens;
                        this.openPopUp("tokensList.fxml");
                        this.tokensListLabel.setText("Take up the same number of students from this card");
                        this.spinnersInit();
                    } else {
                        this.directivesDispatcher.activateJester(this.username, tokens, this.tokensList);
                        this.activatedCharacterCard = false;
                        this.tokensList = null;
                        this.popUpStage.close();
                    }
                }
                case "monk" -> {
                    this.directivesDispatcher.activateMonk(this.username, tokens, this.enlightenedIsland);
                    this.activatedCharacterCard = false;
                    this.popUpStage.close();
                }
                case "bard" -> {
                    if (this.tokensList == null) {
                        this.popUpStage.close();
                        this.tokensList = tokens;
                        this.openPopUp("tokensList.fxml");
                        this.tokensListLabel.setText("Take up the same number of students from your entrance");
                        this.spinnersInit();
                    } else {
                        this.directivesDispatcher.activateBard(this.username, this.tokensList, tokens);
                        this.activatedCharacterCard = false;
                        this.tokensList = null;
                        this.popUpStage.close();
                    }
                }
                case "queen" -> {
                    this.directivesDispatcher.activateQueen(this.username, tokens);
                    this.activatedCharacterCard = false;
                    this.popUpStage.close();
                }
            }
        } else {
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
                            "You have to move exactly %d students from entrance to hall and/or islands",
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
                            "You have to move exactly %d students from entrance to hall and/or islands",
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
    }


    @FXML
    private void moveMotherNature() {
        this.showMessageAlert("Move mother nature",
                String.format("Choose an island by clicking on it (max distance is %d)",
                        this.directivesParser.getDashboardLastDiscardedAssistantCard(this.username).getValue().getValue()
                                + this.directivesParser.getAdditionalMotherNatureSteps()));
    }

    @FXML
    private void enableClouds() {
        this.showMessageAlert("Choose cloud", "Choose a cloud by clicking on it");
    }

    @FXML
    private void chooseCloud(MouseEvent event) {
        if (this.directivesParser.getCurrentPhase().getValue().equals("Choose cloud")) {
            int chosenCloud = Integer.parseInt(((ImageView) event.getSource()).getId().split("cloud")[1]);
            this.directivesDispatcher.actionChooseCloud(this.username, chosenCloud);
        }
    }

    @FXML
    private void activateCharacterCard() {
        this.popUpStage.close();
        if (this.chosenCharacterCard != null) {
            this.activatedCharacterCard = true;
            try {
                GUIGameDisplayer.EFFECT_METHOD_MAPPINGS.get(this.chosenCharacterCard).invoke(this);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void selectTokenColor(MouseEvent event) {
        String color = ((ImageView) event.getSource()).getId();
        if (this.activatedCharacterCard) {
            switch (chosenCharacterCard) {
                case "mushroomer" -> {
                    this.directivesDispatcher.activateMushroomer(this.username,
                            View.AVAILABLE_TOKEN_COLORS.get(color.substring(0, 1)));
                    this.activatedCharacterCard = false;
                }
                case "scoundrel" -> {
                    this.directivesDispatcher.activateScoundrel(this.username,
                            View.AVAILABLE_TOKEN_COLORS.get(color.substring(0, 1)));
                    this.activatedCharacterCard = false;
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
                "Choose an island by clicking on it");
    }

    private void herbalist() {
        this.showMessageAlert("Activate character card: herbalist",
                "Choose an island by clicking on it");
    }

    private void mushroomer() {
        this.showMessageAlert("Activate character card: mushroomer",
                "Choose a color by clicking on island info panel");

    }

    private void scoundrel() {
        this.showMessageAlert("Activate character card: scoundrel",
                "Choose a color by clicking on island info panel");
    }

    private void monk() {
        this.showMessageAlert("Activate character card: monk",
                "Choose an island by clicking on it and move one student");
    }

    private void queen() {
        this.showMessageAlert("Activate character card: queen",
                "Choose one student from this card and move it in your hall");
        this.openPopUp("tokensList.fxml");
        this.tokensListLabel.setText("Take up to one students from this card");
        this.spinnersInit();
        this.tokensList = null;
    }

    private void jester() {
        this.showMessageAlert("Activate character card: jester",
                "Swap students from this card to your entrance");
        this.openPopUp("tokensList.fxml");
        this.tokensListLabel.setText(String.format(
                "Take up to %d students from your entrance and swap them with as many students of this card",
                this.directivesParser.getCharacterCardMultipurposeCounter("jester")));
        this.spinnersInit();
        this.tokensList = null;
    }

    private void bard() {
        this.showMessageAlert("Activate character card: bard",
                "Swap students from your hall to your entrance");
        this.openPopUp("tokensList.fxml");
        this.tokensListLabel.setText(String.format(
                "Take up to %d students from your hall and swap them with as many students of your entrance",
                this.directivesParser.getCharacterCardMultipurposeCounter("bard")));
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