package ftc.shift.sample.services;

import ftc.shift.sample.models.Recipe;
import ftc.shift.sample.models.ShortRecipe;
import ftc.shift.sample.models.User;
import ftc.shift.sample.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class RecipeService {

  private final RecipeRepository recipeRepository;

  @Autowired
  public RecipeService(RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }

  public Recipe provideRecipe(String userId, String recipeId) { return recipeRepository.fetchRecipe(userId, recipeId); }

  public void deleteRecipe(String userId, String recipeId) {
    recipeRepository.deleteRecipe(userId, recipeId);
  }

  public Recipe createRecipe(String userId, Recipe recipe) {
    return recipeRepository.createRecipe(userId, recipe);
  }

  public Collection<ShortRecipe> provideRecipes(String userId) {
    return recipeRepository.getAllRecipes(userId);
  }

  public String getCreatorId(String recipeId) { return recipeRepository.getCreatorId(recipeId); }

  public List<String> addMemberToRecipe(String userId, String recipeId) { return recipeRepository.addMember(userId, recipeId); }
}
