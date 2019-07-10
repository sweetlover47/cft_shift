package ftc.shift.sample.repositories;

import ftc.shift.sample.models.*;

import java.util.Collection;
import java.util.List;

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


  /**
   * опасно для жизни
   */
  List<MemberIngredients> addMember(String userId, String recipeId, MemberIngredients addedIngredients);
}
