
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import se.chalmers.ait.dat215.lab2.Recipe;



public class RecipeSearchController implements Initializable {

    private RecipeBackendController backendController = new RecipeBackendController();
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();
    @FXML private AnchorPane searchPane;
    @FXML private AnchorPane recipeResultsPane;
    @FXML private AnchorPane recipeDetailPane;
    @FXML private AnchorPane recipeShadowPane;
    @FXML private ScrollPane resultsPane;
    @FXML private SplitPane recipeSearchPane;
    @FXML private FlowPane recipeListFlowPane;

    @FXML private ComboBox mainIngredientBox;
    @FXML private ComboBox cuisineBox;
    @FXML private RadioButton allDifficultyButton;
    @FXML private RadioButton easyDifficultyButton;
    @FXML private RadioButton mediumDifficultyButton;
    @FXML private RadioButton hardDifficultyButton;
    @FXML private Spinner maxPriceSpinner;
    @FXML private Slider maxTimeSlider;

    @FXML private Label recipeTimeLabel;
    @FXML private Label recipeDetailLabel;
    @FXML private ImageView recipeDetailImage;
    @FXML private Button recipeDetailCloseButton;
    @FXML private ImageView recipeDetailCuisineImage;
    @FXML private ImageView recipeDetailMainIngredientImage;
    @FXML private ImageView recipeDetailDifficultyImage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backendInitialize(url,rb);
        mainIngredientBoxInitialize();
        cuisineBoxInitialize();
        toggleInitialize();
        maxPriceSpinnerInit();
        maxTimeSliderInit();
        populateMainIngredientBox();
        populateCuisineBox();
        Platform.runLater(()->mainIngredientBox.requestFocus());
    }

    private void backendInitialize(URL url,ResourceBundle rb) {
        backendController.initialize(url, rb);
        for (Recipe recipe : backendController.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe,this);
            recipeListItemMap.put(recipe.getName(),recipeListItem);
        }
        this.updateRecipeList();
    }

    private void mainIngredientBoxInitialize() {
        mainIngredientBox.getItems().addAll("Visa Alla","Kött","Fisk","Kyckling","Vegetarisk");
        mainIngredientBox.getSelectionModel().select("Visa Alla");
        mainIngredientBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println((String)newValue);
                backendController.setMainIngredient((String)newValue);
                updateRecipeList();
            }
        });
    }

    private void cuisineBoxInitialize() {
        cuisineBox.getItems().addAll("Visa Alla","Sverige","Grekland","Italien","Asien","Afrika","Frankrike");
        cuisineBox.getSelectionModel().select("Visa Alla");
        cuisineBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                backendController.setCuisine((String)newValue);
                updateRecipeList();
            }
        });
    }

    private void toggleInitialize() {
        ToggleGroup difficultyToggleGroup = new ToggleGroup();
        allDifficultyButton.setToggleGroup(difficultyToggleGroup);
        easyDifficultyButton.setToggleGroup(difficultyToggleGroup);
        mediumDifficultyButton.setToggleGroup(difficultyToggleGroup);
        hardDifficultyButton.setToggleGroup(difficultyToggleGroup);
        allDifficultyButton.setSelected(true);
        difficultyToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (difficultyToggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                    backendController.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });
    }

    private void maxPriceSpinnerInit() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,200,0,5);
        maxPriceSpinner.setValueFactory(valueFactory);
        maxPriceSpinner.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                backendController.setMaxTime((Integer) newValue);
                updateRecipeList();
            }
        });
        maxPriceSpinner.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    //focus gained - do nothing
                }
                else {
                    Integer value = Integer.valueOf(maxPriceSpinner.getEditor().getText());
                    backendController.setMaxPrice(value);
                    updateRecipeList();
                }
            }
        });
    }

    private void maxTimeSliderInit() {
        maxTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                recipeTimeLabel.setText(((Integer)(int) maxTimeSlider.getValue()).toString() + " min");
                if (newValue != null && !newValue.equals(oldValue) && !maxTimeSlider.isValueChanging()) {
                    backendController.setMaxTime(newValue.intValue());
                    updateRecipeList();
                }
            }
        });
    }

    private void updateRecipeList() {
        recipeListFlowPane.getChildren().clear();
        List<Recipe> recipes = backendController.getRecipes();
        for (Recipe recipe : recipes) {
            RecipeListItem recipeListItem = recipeListItemMap.get(recipe.getName());
            recipeListFlowPane.getChildren().add(recipeListItem);
        }
    }

    private void populateRecipeDetailView(Recipe recipe) {
        recipeDetailImage.setImage(recipe.getFXImage());
        recipeDetailLabel.setText(recipe.getName());
        recipeDetailCuisineImage.setImage(getCuisineImage(recipe.getCuisine()));
        recipeDetailMainIngredientImage.setImage(getMainIngredientImage(recipe.getMainIngredient()));
        recipeDetailDifficultyImage.setImage(getDifficultyImage(recipe.getDifficulty()));
    }


    @FXML
    public void closeRecipeView() {
        recipeSearchPane.toFront();
    }


    public void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        recipeShadowPane.toFront();
        recipeDetailPane.toFront();
    }

    private void populateMainIngredientBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                        if(item == null || empty) {
                            setGraphic(null);
                        }
                        else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {
                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                // Insert code for loading default picture here
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngredientBox.setButtonCell(cellFactory.call(null));
        mainIngredientBox.setCellFactory(cellFactory);
    }

    private void populateCuisineBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                        if(item == null || empty) {
                            setGraphic(null);
                        }
                        else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {
                                    case "Afrika":
                                        iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Asien":
                                        iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Frankrike":
                                        iconPath = "RecipeSearch/resources/icon_flag_france.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Grekland":
                                        iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Italien":
                                        // Should be indian, probably specified wrong cuisine in previous lab assignment
                                        iconPath = "RecipeSearch/resources/icon_flag_india.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Sverige":
                                        iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                // Insert code for loading default picture here
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        cuisineBox.setButtonCell(cellFactory.call(null));
        cuisineBox.setCellFactory(cellFactory);
    }

    public Image getCuisineImage(String cuisine) {
        String iconPath;
        switch (cuisine) {
            case "Afrika":
                iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Asien":
                iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Frankrike":
                iconPath = "RecipeSearch/resources/icon_flag_france.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Grekland":
                iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Italien":
                // Should be indian, probably specified wrong cuisine in previous lab assignment
                iconPath = "RecipeSearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Sverige":
                iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        // Should return a default image, fix later(most likely never)
        return null;
    }

    public Image getMainIngredientImage(String mainIngredient) {
        String iconPath;
        switch (mainIngredient) {
            case "Kött":
                iconPath = "RecipeSearch/resources/icon_main_meat.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Fisk":
                iconPath = "RecipeSearch/resources/icon_main_fish.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Kyckling":
                iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Vegetarisk":
                iconPath = "RecipeSearch/resources/icon_main_veg.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        // Should return a default image, fix later(most likely never)
        return null;
    }

    public Image getDifficultyImage(String difficulty) {
        String iconPath;
        switch (difficulty) {
            case "Lätt":
                iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Mellan":
                iconPath = "RecipeSearch/resources/icon_difficulty_medium.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Svår":
                iconPath = "RecipeSearch/resources/icon_difficulty_hard.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        return null;
    }


}