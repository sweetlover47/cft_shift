package ftc.shift.sample.repositories;

import ftc.shift.sample.models.Recipe;
import ftc.shift.sample.models.ShortRecipe;
import ftc.shift.sample.models.User;

import java.util.Collection;
import java.util.List;

/**
 * Интерфейс для получения данных по книгам
 */
public interface RecipeRepository {

  Recipe fetchRecipe(String userId, String bookId);

  void deleteRecipe(String userId, String bookId);

  Recipe createRecipe(String userId, Recipe recipe);

  Collection<ShortRecipe> getAllRecipes(String userId);

  String getCreatorId(String recipeId);

  List<String> addMember(String userId, String recipeId);
}
