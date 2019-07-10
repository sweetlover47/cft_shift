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

    public List<ShortRecipe> findRecipesByTitle(String title) {
        Collection<ShortRecipe> allRecipes = recipeRepository.getAllShortRecipes();
        List<ShortRecipe> findedRecipes = new LinkedList<>();

        for (ShortRecipe recipe : allRecipes) {
            // если подстрока title присутствует в назавние рецепта, добавить в список
            if (recipe.getTitle().lastIndexOf(title) != -1) {
                findedRecipes.add(recipe);
            }
        }

        return findedRecipes;
    }

    public List<ShortRecipe> findRecipesByIngredient(String title) {
        Collection<Recipe> allRecipes = recipeRepository.getAllRecipes();
        List<ShortRecipe> findedRecipes = new LinkedList<>();

        // need add logic!
        for (Recipe recipe : allRecipes) {


            findedRecipes.add(recipe);
        }

        return findedRecipes;
    }
}
