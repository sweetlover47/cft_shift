package ftc.shift.sample.repositories;

import ftc.shift.sample.exception.NotFoundException;
import ftc.shift.sample.models.Recipe;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Реализиция, хранящая все данные в памяти приложения
 */
@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "false")
public class InMemoryRecipeRepository implements RecipeRepository {
  /**
   * Ключ - имя пользователя, значение - все книги, которые есть у пользователя
   */
  private Map<String, Map<String, Recipe>> bookCache = new HashMap<>();
  private Map<String, String> recipeCreatorMap = new HashMap<>();
  private static Integer countRecipes = 1;
  public InMemoryRecipeRepository() {   /* У каждого пользователя есть список его рецептов. У каждого рецепта есть id ( первое значение в HashMap ) и сам рецепт ( массив строк ) */
    // Заполним репозиторий тестовыми данными
    // В тестовых данных существует всего 3 пользователя: UserA / UserB / UserC

    bookCache.put("User1", new HashMap<>());
    /*bookCache.get("User1").put("1", new Recipe("1", "Борщ", "User1", "Великолепный и вкусный суп", 1,
            Collections.singletonList("Фантастика")));
    recipeCreatorMap.put("1", "User1");*/
    /*bookCache.get("UserA").put("2", new Recipe("2", "Название 2", "Автор Писателевич", 48,
            Collections.singletonList("Детектив"))); */                                       /* Collections.singletonList создает массив данных в JSON */

    bookCache.put("User2", new HashMap<>());
    /*bookCache.get("UserB").put("3", new Recipe("3", "Название 3", "Писатель Авторович", 24,
            Collections.singletonList("Киберпанк")));*/

    bookCache.put("User3", new HashMap<>());
  }

  @Override
  public Recipe fetchRecipe(String userId, String bookId) {
    if (!bookCache.containsKey(userId)) {
      // Пользователь не найден
      throw new NotFoundException();
    }

    Map<String, Recipe> userBooks = bookCache.get(userId);

    if (!userBooks.containsKey(bookId)) {
      // У пользователя не найдена книга
      throw new NotFoundException();
    }

    return userBooks.get(bookId);
  }

  @Override
  public Recipe updateRecipe(String userId, String recipeId, Recipe recipe) {
    if (!bookCache.containsKey(userId)) {
      // Пользователь не найден
      throw new NotFoundException();
    }

    Map<String, Recipe> userBooks = bookCache.get(userId);

    if (!userBooks.containsKey(recipeId)) {
      // У пользователя не найдена книга
      throw new NotFoundException();
    }

    recipe.setId(recipeId);
    userBooks.put(recipeId, recipe);
    return recipe;
  }

  @Override
  public void deleteRecipe(String userId, String recipeId) {
    if (!bookCache.containsKey(userId)) {
      // Пользователь не найден
      throw new NotFoundException();
    }

    Map<String, Recipe> userBooks = bookCache.get(userId);

    if (!userBooks.containsKey(recipeId)) {
      // У пользователя не найдена книга
      throw new NotFoundException();
    }
    /* countRecipes - 1 */
    bookCache.remove(recipeId);
  }

  @Override
  public Recipe createRecipe(String userId, Recipe recipe) {  /* Добавление нового рецепта */
    if (!bookCache.containsKey(userId)) {
      // Пользователь не найден
      throw new NotFoundException();
    }

    Map<String, Recipe> userBooks = bookCache.get(userId);
    // Плохой способ генерирования случайных идентификаторов, использовать только для примеров
    recipe.setId(countRecipes.toString());
    countRecipes++;
    userBooks.put(recipe.getId(), recipe);
    recipeCreatorMap.put(recipe.getId(), userId);
    return recipe;
  }

  @Override
  public Collection<Recipe> getAllRecipes(String userId) {
    /*if (!bookCache.containsKey(userId)) {
      // Пользователь не найден
      throw new NotFoundException();
    }

    return bookCache.get(userId).values();*/

    List<Recipe> list = new LinkedList<>();
    for(Map.Entry<String, Map<String, Recipe>> entry: bookCache.entrySet()) {
      for (Recipe r : entry.getValue().values())
        list.add(r);
    }

    return list;

  }

  @Override
  public String getCreatorId(String recipeId) {
    return recipeCreatorMap.get(recipeId);
  }
}
