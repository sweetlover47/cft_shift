package ftc.shift.sample.services;

import ftc.shift.sample.models.*;
import ftc.shift.sample.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe provideRecipe(String userId, String recipeId) {
        return recipeRepository.fetchRecipe(userId, recipeId);
    }

    public void deleteRecipe(String userId, String recipeId) {
        recipeRepository.deleteRecipe(userId, recipeId);
    }

    public Recipe createRecipe(String userId, Recipe recipe) {
        return recipeRepository.createRecipe(userId, recipe);
    }

    public Collection<ShortRecipe> provideRecipes(String userId) {
        return recipeRepository.getAllShortRecipes();
    }

    public String getCreatorId(String recipeId) {
        return recipeRepository.getCreatorId(recipeId);
    }

    public List<MemberIngredients> addMemberToRecipe(String userId, String recipeId, MemberIngredients addedIngredients) {
        return recipeRepository.addMember(userId, recipeId, addedIngredients);
    }

    public List<ShortRecipe> findRecipes(String filter) {
        Collection<Recipe> allRecipes = recipeRepository.getAllRecipes();
        List<ShortRecipe> findedRecipes = new LinkedList<>();

        for (Recipe recipe : allRecipes) {
            // если подстрока title присутствует в назавние рецепта, добавить в список
            // find in recipe name
            if (recipe.getTitle().lastIndexOf(filter) != -1) {
                findedRecipes.add(recipe);
                continue;
            }

            // find in ingredients
            List<Ingredient> ingredients = recipe.getIngredients();
            for(Ingredient ingredient : ingredients) {
                if(ingredient.getName().lastIndexOf(filter) != -1) {
                    findedRecipes.add(recipe);
                    continue;
                }
            }
        }

        return findedRecipes;
    }


    public List<AddedIngredient> getFridge(String userId) {
        return recipeRepository.getUserFridge(userId);
    }

    public List<AddedIngredient> addIngredientInFridge(String userId, AddedIngredient newIng) {
        return recipeRepository.addIngredientInFridge(userId, newIng);
    }


    public List<AddedIngredient> delIngredientFromFridge(String userId, AddedIngredient delIng) {
        return recipeRepository.delIngredientFromFridge(userId, delIng);
    }


}
