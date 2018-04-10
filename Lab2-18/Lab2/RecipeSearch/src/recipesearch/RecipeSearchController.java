
package recipesearch;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;


public class RecipeSearchController implements Initializable {

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    @FXML private AnchorPane searchPane;
    @FXML private AnchorPane recipeDetailPane;
    @FXML private ScrollPane resultsPane;
    @FXML private FlowPane recipeListFlowPane;

    @FXML private ComboBox mainIngredientBox;
    @FXML private ComboBox cuisineBox;
    @FXML private RadioButton allDifficultyButton;
    @FXML private RadioButton easyDifficultyButton;
    @FXML private RadioButton mediumDifficultyButton;
    @FXML private RadioButton hardDifficultyButton;
    @FXML private Spinner maxPriceSpinner;
    @FXML private Slider maxTimeSlider;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.updateRecipeList();
    }

    private void updateRecipeList() {
        recipeListFlowPane.getChildren().clear();
        List<Recipe> recipes = db.search(new SearchFilter(null,0,null,0,null));
        for (Recipe recipe : recipes) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe,this);
            recipeListFlowPane.getChildren().add(recipeListItem);
        }
    }

}