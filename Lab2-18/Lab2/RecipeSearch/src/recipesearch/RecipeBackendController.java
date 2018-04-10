package recipesearch;

import javafx.fxml.Initializable;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class RecipeBackendController implements Initializable {
    private RecipeDatabase db;
    private String cuisine;
    private String mainIngredient;
    private String difficulty;
    private int maxPrice;
    private int maxTime;
    private List<String>allowedCuisines = new ArrayList<>();
    private List<String>allowedMainIngredient = new ArrayList<>();
    private List<String>allowedDifficulties = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        db = RecipeDatabase.getSharedInstance();
        allowedCuisines.addAll(Arrays.asList("Sverige","Grekland","Italien","Asien","Afrika","Frankrike"));
        allowedMainIngredient.addAll(Arrays.asList("Kött","Fisk","Kyckling","Vegetarisk"));
        allowedDifficulties.addAll(Arrays.asList("Lätt","Mellan","Svår"));
        this.setDifficulty("Lätt");
        this.setMaxTime(0);
        this.setCuisine("Sverige");
        this.setMaxPrice(0);
        this.setMainIngredient("null");

    }

    public List<Recipe> getRecipes() {
        return db.search(new SearchFilter(difficulty,maxTime,cuisine,maxPrice,mainIngredient));
    }

    public void setCuisine(String cuisine) {
        if (allowedCuisines.contains(cuisine)) {
            this.cuisine = cuisine;
        }
        else {
            this.cuisine = "null";
        }
    }

    public void setMainIngredient(String mainIngredient) {
        if (allowedMainIngredient.contains(mainIngredient)) {
            this.mainIngredient = mainIngredient;
        }
        else {
            this.mainIngredient = "null";
        }
    }

    public void setDifficulty(String difficulty) {
        if (allowedDifficulties.contains(difficulty)) {
            this.difficulty = difficulty;
        }
        else {
            this.difficulty = "null";
        }
    }

    public void setMaxPrice(int maxPrice) {
        if (maxPrice > 0) {
            this.maxPrice = maxPrice;
        }
        else {
            this.maxPrice = 0;
        }
    }

    public void setMaxTime(int maxTime) {
        if (maxTime <= 150 && maxTime % 10 == 0) {
            this.maxTime = maxTime;
        }
        else {
            this.maxTime = 0;
        }
    }


}
