package recipesearch;

import javafx.fxml.Initializable;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RecipeBackendController implements Initializable {
    private RecipeDatabase db;
    private String cuisine;
    private String mainIngredient;
    private String difficulty;
    private int maxPrice;
    private int maxTime;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        db = RecipeDatabase.getSharedInstance();
        this.cuisine = null;
        this.mainIngredient = null;
        this.difficulty = null;
        this.maxPrice = 0;
        this.maxTime = 0;

    }

    public List<Recipe> getRecipes() {
        return db.search(new SearchFilter(difficulty,maxTime,cuisine,maxPrice,mainIngredient));
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }


}
