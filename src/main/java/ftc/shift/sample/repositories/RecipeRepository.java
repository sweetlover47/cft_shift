package ftc.shift.sample.repositories;

import ftc.shift.sample.models.Recipe;

import java.util.Collection;

/**
 * Интерфейс для получения данных по книгам
 */
public interface RecipeRepository {

  Recipe fetchRecipe(String userId, String bookId);

  Recipe updateRecipe(String userId, String bookId, Recipe recipe);

  void deleteRecipe(String userId, String bookId);

  Recipe createRecipe(String userId, Recipe recipe);

  Collection<Recipe> getAllRecipes(String userId);

  String getCreatorId(String recipeId);
}
