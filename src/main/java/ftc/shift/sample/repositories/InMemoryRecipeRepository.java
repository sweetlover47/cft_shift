package ftc.shift.sample.repositories;

import ftc.shift.sample.exception.NotFoundException;
import ftc.shift.sample.models.Recipe;
import ftc.shift.sample.models.ShortRecipe;
import ftc.shift.sample.models.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.util.*;
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
    private Map<User, Map<String, Recipe>> bookCache = new HashMap<>();
    private Map<String, String> recipeCreatorMap = new HashMap<>();
    private static Integer countRecipes = 1;

    public InMemoryRecipeRepository() {   /* У каждого пользователя есть список его рецептов. У каждого рецепта есть id ( первое значение в HashMap ) и сам рецепт ( массив строк ) */
        // Заполним репозиторий тестовыми данными
        // В тестовых данных существует всего 3 пользователя: UserA / UserB / UserC

        bookCache.put(new User("User1", "89990112554"), new HashMap<>());
        /*Recipe recipe = new Recipe("1", "Борщ", "UserA", "Великолепный и вкусный суп", 1, *//*new ArrayList<String>(),*//* new ArrayList<>());
        this.createRecipe("User1", recipe);*/
        bookCache.put(new User("User2", "84561112545"), new HashMap<>());
        bookCache.put(new User("User3", "89083271800"), new HashMap<>());
    }

    @Override
    public Recipe fetchRecipe(String userId, String bookId) {   /* Получить конкретный рецепт (complete) */
        Set<User> users = bookCache.keySet();
        Set<User> filtered = users.stream().filter(us -> us.getUserId().equals(userId)).collect(Collectors.toSet());
        if (filtered.size() != 1) {
            // Пользователь не найден
            throw new NotFoundException();
        }
        User user = (User) filtered.toArray()[0];
        Map<String, Recipe> userBooks = bookCache.get(user);

        if (!userBooks.containsKey(bookId)) {
            // У пользователя не найдена книга
            throw new NotFoundException();
        }
        return userBooks.get(bookId);
    }

    @Override
    public List<String> addMember(String userId, String recipeId) {
                                                                            /* Получить User по userId */
        Set<User> users = bookCache.keySet();
        Set<User> filtered = users.stream().filter(us -> us.getUserId().equals(userId)).collect(Collectors.toSet());
        if (filtered.size() != 1) {
            // Пользователь не найден
            throw new NotFoundException();
        }
        User user = (User) filtered.toArray()[0];
        String creatorId = getCreatorId(recipeId);
        Recipe recipe = fetchRecipe(creatorId, recipeId);                   /* Нужно найти тот рецепт, на котором мы сейчас находимся */
        List<User> memberList = recipe.getMembers();
        if (!memberList.contains(user))
            memberList.add(user);
        recipe.setMembers(memberList);
        ArrayList<String> ar = new ArrayList<>();
        for (User elem : memberList)
            ar.add(elem.getUserId());
        return ar;
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
    public Recipe createRecipe(String userId, Recipe recipe) {  /* Добавление нового рецепта (complete) */
        Set<User> users = bookCache.keySet();
        Set<User> filtered = users.stream().filter(us -> us.getUserId().equals(userId)).collect(Collectors.toSet());
        if (filtered.size() != 1) {
            // Пользователь не найден
            throw new NotFoundException();
        }
        User user = (User) filtered.toArray()[0];
        Map<String, Recipe> userBooks = bookCache.get(user);
        recipe.setMembers(new LinkedList<>());
        userBooks.put(recipe.getId(), recipe);
        recipeCreatorMap.put(recipe.getId(), userId);
        return recipe;
    }

    @Override
    public Collection<ShortRecipe> getAllRecipes(String userId) {
        List<ShortRecipe> list = new LinkedList<>();
        for (Map.Entry<User, Map<String, Recipe>> entry : bookCache.entrySet()) {
            for (ShortRecipe r : entry.getValue().values()) {
                ShortRecipe sr = new ShortRecipe(r.getId(), r.getTitle(), r.getDescription(), r.getStatus());
                list.add(sr);
            }
        }
        return list;
    }

    @Override
    public String getCreatorId(String recipeId) {
        return recipeCreatorMap.get(recipeId);
    }



    /*
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
    }*/
}
