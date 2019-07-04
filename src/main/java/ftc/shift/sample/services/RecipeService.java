package ftc.shift.sample.services;

import ftc.shift.sample.models.Recipe;
import ftc.shift.sample.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RecipeService {

  private final RecipeRepository recipeRepository;

  @Autowired
  public RecipeService(RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }

  public Recipe provideRecipe(String userId, String recipeId) { return recipeRepository.fetchRecipe(userId, recipeId); }

  public Recipe updateRecipe(String userId, String bookId, Recipe recipe) {
    return recipeRepository.updateRecipe(userId, bookId, recipe);
  }

  public void deleteRecipe(String userId, String recipeId) {
    recipeRepository.deleteRecipe(userId, recipeId);
  }

  public Recipe createRecipe(String userId, Recipe recipe) {
    return recipeRepository.createRecipe(userId, recipe);
  }

  public Collection<Recipe> provideRecipes(String userId) {
    return recipeRepository.getAllRecipes(userId);
  }

  public String getCreatorId(String recipeId) { return recipeRepository.getCreatorId(recipeId); }
}
