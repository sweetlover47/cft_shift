package ftc.shift.sample.repositories;

import ftc.shift.sample.exception.NotFoundException;
import ftc.shift.sample.models.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Реализиция, хранящая все данные в памяти приложения
 */
@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "false")
public class InMemoryRecipeRepository implements RecipeRepository {
    /**
     * Ключ - имя пользователя, значение - все книги, которые есть у пользователя
     */
    private Map<User, Map<String, Recipe>> recipeData = new HashMap<>();
    private Map<String, String> recipeCreatorMap = new HashMap<>();
    private static AtomicInteger countRecipes = new AtomicInteger(0);

    public InMemoryRecipeRepository() {
        /* У каждого пользователя есть список его рецептов. У каждого рецепта есть id ( первое значение в HashMap ) и сам рецепт ( массив строк ) */
        recipeData.put(new User("Петя", "1"), new HashMap<>());
        /*Recipe recipe = new Recipe("1", "Борщ", "UserA", "Великолепный и вкусный суп", 1, new ArrayList<String>(), new ArrayList<>());
        this.createRecipe("User1", recipe);*/
        recipeData.put(new User("Вася", "2"), new HashMap<>());
        recipeData.put(new User("Катя", "3"), new HashMap<>());
    }


    @Override
    public Recipe fetchRecipe(String userId, String bookId) {
        User user = fetchUserByUserId(userId);
        Map<String, Recipe> userBooks = recipeData.get(user);

        if (!userBooks.containsKey(bookId)) {
            // У пользователя не найдена книга
            throw new NotFoundException();
        }
        return userBooks.get(bookId);
    }

    @Override
    public Map<User, List<MemberIngredient>> addMember(String userId, String recipeId, MemberIngredient memberIngredient) {
        User user = fetchUserByUserId(userId);
        String creatorId = getCreatorId(recipeId);
        Recipe recipe = fetchRecipe(creatorId, recipeId);                   /* Нужно найти тот рецепт, на котором мы сейчас находимся */
        Map<User, List<MemberIngredient>> memberList = recipe.getMembers();
        if (!memberList.containsKey(user)) {
            List<MemberIngredient> ingredientList = new ArrayList<>();
            ingredientList.add(memberIngredient);
            memberList.put(user, ingredientList);
        }
        else if (memberList.containsKey(user) && !memberList.get(user).contains(memberIngredient)) {
            List<MemberIngredient> ingredientList = new ArrayList<>();
            ingredientList.add(memberIngredient);
            memberList.put(user, ingredientList);
        }
        recipe.setMembers(memberList);
        Map<User, List<MemberIngredient>> ar = new HashMap<>();
        for (User elem : memberList.keySet())
            ar.put(elem, memberList.get(elem));
        //Добавить список ингредиентов
        return ar;
    }

    @Override
    public void deleteRecipe(String userId, String recipeId) {
        if (!recipeData.containsKey(userId)) {
            // Пользователь не найден
            throw new NotFoundException();
        }

        Map<String, Recipe> userBooks = recipeData.get(userId);

        if (!userBooks.containsKey(recipeId)) {
            // У пользователя не найдена книга
            throw new NotFoundException();
        }
        /* countRecipes - 1 */
        recipeData.remove(recipeId);
    }

    @Override
    public Recipe createRecipe(String userId, Recipe recipe) {  /* Добавление нового рецепта (complete) */
        User user = fetchUserByUserId(userId);
        Map<String, Recipe> userBooks = recipeData.get(user);
        recipe.setMembers(new HashMap<>());
        recipe.setId(Integer.toString(countRecipes.getAndIncrement()));
        userBooks.put(recipe.getId(), recipe);
        recipeCreatorMap.put(recipe.getId(), userId);
        return recipe;
    }

    @Override
    public Collection<ShortRecipe> getAllShortRecipes() {
        List<ShortRecipe> list = new LinkedList<>();
        for (Map.Entry<User, Map<String, Recipe>> entry : recipeData.entrySet()) {
            for (ShortRecipe r : entry.getValue().values()) {
                ShortRecipe sr = new ShortRecipe(r.getId(), r.getTitle(), r.getDescription(), r.getStatus());
                list.add(sr);
            }
        }
        return list;
    }

    @Override
    public Collection<Recipe> getAllRecipes() {
        List<Recipe> list = new LinkedList<>();
        for (Map.Entry<User, Map<String, Recipe>> entry : recipeData.entrySet()) {
            for (Recipe r : entry.getValue().values()) {
                list.add(r);
            }
        }
        return list;
    }

//----------------------------------------------------------------------------------------

    @Override
    public String getCreatorId(String recipeId) {
        return recipeCreatorMap.get(recipeId);
    }

    private User fetchUserByUserId(String userId) {
        Set<User> users = recipeData.keySet();
        return users.stream()
                .filter(us -> us.getUserId().equals(userId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
}
