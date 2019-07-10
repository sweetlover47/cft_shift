package ftc.shift.sample.repositories;

import ftc.shift.sample.exception.NoIngredientException;
import ftc.shift.sample.exception.NotCreatorRecipe;
import ftc.shift.sample.exception.NotFoundException;
import ftc.shift.sample.models.*;
import io.swagger.models.auth.In;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
        User user = new User("UserA", "1");
        recipeData.put(user, new HashMap<>());
        List<Ingredient> list = new ArrayList<>();
        list.add(new Ingredient("onion", "0", "2"));
        list.add(new Ingredient("onion2", "0", "4"));
        List<AddedIngredient> addedIngredients = new ArrayList<>();
        addedIngredients.add(new AddedIngredient("onion", "1"));
        List<MemberIngredients> map = new ArrayList<>();
        Recipe recipe = new Recipe("1", "qwe", user, "qweqweqwe", "wait", list, map);
        createRecipe("1", recipe);
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
    public List<MemberIngredients> addMember(String userId, String recipeId, MemberIngredients memberIngredients) {
        User user = memberIngredients.getUser();
        Recipe recipe = fetchRecipe(getCreatorId(recipeId), recipeId);
        MemberIngredients userIngredients = recipe.getMembers().stream()
                .filter(mi -> mi.getUser().equals(user))
                .findAny()
                .orElse(null);
        if (userIngredients == null) {
            List<MemberIngredients> members = recipe.getMembers();
            members.add(new MemberIngredients(user, new ArrayList<>()));
            recipe.setMembers(members);
            userIngredients = recipe.getMembers().stream()
                    .filter(mi -> mi.getUser().equals(user))
                    .findAny()
                    .orElseThrow(ExceptionInInitializerError::new);
        }
        Integer value;
        String oldValue;
        for (AddedIngredient ai : memberIngredients.getIngredients()) {
            boolean addedExisting = userIngredients.getIngredients() != null &&userIngredients.getIngredients().stream()
                    .anyMatch(ingred -> ingred.getName().equals(ai.getName()));
            List<AddedIngredient> addedIngredientList = userIngredients.getIngredients();
            if (!addedExisting) {
                addedIngredientList.add(ai);
                userIngredients.setIngredients(addedIngredientList);
                oldValue = recipe.getIngredients().stream()
                        .filter(in -> in.getName().equals(ai.getName()))
                        .findAny()
                        .orElseThrow(NoIngredientException::new)
                        .getCountHave();
            } else {
                AddedIngredient addedIngredient = addedIngredientList.stream()
                        .filter(addedi -> addedi.getName().equals(ai.getName()))
                        .findAny()
                        .orElseThrow(RuntimeException::new);
                oldValue = addedIngredient.getCount();
                value = Integer.valueOf(ai.getCount()) + Integer.valueOf(oldValue);
                ai.setCount(Integer.toString(value));
                addedIngredientList.remove(addedIngredient);
                addedIngredientList.add(ai);
                userIngredients.setIngredients(addedIngredientList);
            }
            Ingredient ingredient = recipe.getIngredients().stream()
                    .filter(p -> p.getName().equals(ai.getName()))
                    .findAny()
                    .orElseThrow(NotFoundException::new);
            Integer addedValue = Integer.valueOf(ai.getCount()) - Integer.valueOf(oldValue);
            value = Integer.valueOf(ingredient.getCountHave()) + addedValue;
            ingredient.setCountHave(Integer.toString(value));
        }
        return recipe.getMembers();
    }
/*
    @Override
    public List<MemberIngredients> addMember(String userId, String recipeId, MemberIngredients memberIngredients) {
        User user = memberIngredients.getUser();
        String creatorId = getCreatorId(recipeId);
        Recipe recipe = fetchRecipe(creatorId, recipeId);
        {
            Optional<MemberIngredients> memberList = recipe.getMembers().stream()
                    .filter(mi -> mi.getUser().equals(user))
                    .findAny();                 // Получаем MemberIngredients для конкретного юзера
            if (!memberList.isPresent()) {
                List<MemberIngredients> members = recipe.getMembers();
                members.add(new MemberIngredients(user, memberIngredients.getIngredients()));
                recipe.setMembers(members);
                // сделать добавление в CountHave
            }
        }
        MemberIngredients memberList = recipe.getMembers().stream()     // Сделано так, тк я хочу то же название, что и для Optional'a выше
                .filter(us -> us.getUser().equals(user))
                .findAny()
                .orElseThrow(NotFoundException::new);
        Integer value;
        for (AddedIngredient ai : memberIngredients.getIngredients()) { // Каждый user может добавлять несколько ингредиентов, поэтому проходимся циклом по добавленным ингредиентам
            boolean addedExisting = memberList.getIngredients().stream()
                    .anyMatch(us -> us.getName().equals(ai.getName()));
            if (!addedExisting) {
                List<AddedIngredient> ingredientList = memberList.getIngredients();
                ingredientList.add(ai);
                memberList.setIngredients(ingredientList);
            } else {
                AddedIngredient addedIngredient = memberList.getIngredients().stream()
                        .filter(p -> p.getName().equals(ai.getName()))
                        .findAny()
                        .orElseThrow(NotFoundException::new);
                String oldValue = memberList.getIngredients().stream()      // Старое значение ингредиента, добавленного ранее
                        .filter(p -> p.getName().equals(ai.getName()))
                        .findAny()
                        .orElseThrow(NotFoundException::new)
                        .getCount();
                value = Integer.valueOf(ai.getCount()) + Integer.valueOf(oldValue);
                ai.setCount(Integer.toString(value));
                List<AddedIngredient> list = memberList.getIngredients();
                list.remove(addedIngredient);
                list.add(ai);
                memberList.setIngredients(list);
            }
            Ingredient ingredient = recipe.getIngredients().stream()
                    .filter(p -> p.getName().equals(ai.getName()))
                    .findAny()
                    .orElseThrow(NotFoundException::new);
            value = Integer.valueOf(ingredient.getCountHave()) + Integer.valueOf(ai.getCount());
            ingredient.setCountHave(Integer.toString(value));
        }
        return recipe.getMembers();
    }
*/
    @Override
    public void deleteRecipe(String userId, String recipeId) {
        User user = fetchUserByUserId(userId);
        if (!recipeData.containsKey(user)) {
            // Пользователь не найден
            throw new NotFoundException();
        }
        if (!userId.equals(getCreatorId(recipeId))) {
            //Пользователь не является создателем рецепта
            throw new NotCreatorRecipe();
        }
        Map<String, Recipe> userRecipes = recipeData.get(user);
        if (!userRecipes.containsKey(recipeId)) {
            // У пользователя не найден рецепт
            throw new NotFoundException();
        }
        userRecipes.remove(recipeId, userRecipes.get(user));
        recipeData.replace(user, userRecipes);
    }

    @Override
    public Recipe createRecipe(String userId, Recipe recipe) {  /* Добавление нового рецепта (complete) */
        User user = fetchUserByUserId(userId);
        Map<String, Recipe> userBooks = recipeData.get(user);
        if (recipe.getMembers() == null)
            recipe.setMembers(new ArrayList<>());
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
