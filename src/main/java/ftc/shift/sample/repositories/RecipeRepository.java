package ftc.shift.sample.repositories;

import ftc.shift.sample.models.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Интерфейс для получения данных по книгам
 */
public interface RecipeRepository {

  Recipe fetchRecipe(String userId, String bookId);

  void deleteRecipe(String userId, String bookId);

  Recipe createRecipe(String userId, Recipe recipe);

  Collection<ShortRecipe> getAllShortRecipes();

  Collection<Recipe> getAllRecipes();

  String getCreatorId(String recipeId);

  Map<User, List<MemberIngredient>> addMember(String userId, String recipeId, MemberIngredient memberIngredient);
}
