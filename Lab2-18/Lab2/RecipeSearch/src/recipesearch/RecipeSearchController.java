
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private RecipeBackendController backendController = new RecipeBackendController();
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();
    @FXML private AnchorPane searchPane;
    @FXML private AnchorPane recipeResultsPane;
    @FXML private AnchorPane recipeDetailPane;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backendController.initialize(url, rb);
        for (Recipe recipe : backendController.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe,this);
            recipeListItemMap.put(recipe.getName(),recipeListItem);
        }
        this.updateRecipeList();
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
        cuisineBox.getItems().addAll("Visa Alla","Sverige","Grekland","Italien","Asien","Afrika","Frankrike");
        cuisineBox.getSelectionModel().select("Visa Alla");
        cuisineBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                backendController.setCuisine((String)newValue);
                updateRecipeList();
            }
        });
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
        maxTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue != null && !newValue.equals(oldValue) && !maxTimeSlider.isValueChanging()) {
                    backendController.setMaxTime(newValue.intValue());
                    updateRecipeList();
                    recipeTimeLabel.setText(newValue.toString());
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
    }


    @FXML
    public void closeRecipeView() {
        recipeSearchPane.toFront();
    }


    public void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        recipeDetailPane.toFront();
    }

}