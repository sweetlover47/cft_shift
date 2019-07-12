package ftc.shift.sample.repositories;

import ftc.shift.sample.exception.NotCreatorRecipeException;
import ftc.shift.sample.exception.NotFoundException;
import ftc.shift.sample.models.*;
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
        /* У каждого пользователя есть список его рецептов. У каждого рецепта есть id ( первое значение в HashMap ) и сам рецепт*/
        recipeData.put(new User("Петя", "1", new ArrayList<>()), new HashMap<>());
        recipeData.put(new User("Вася", "2", new ArrayList<>()), new HashMap<>());
        recipeData.put(new User("Катя", "3", new ArrayList<>()), new HashMap<>());
        recipeData.put(new User("UserA", "+79293889399", new ArrayList<>()), new HashMap<>());
        recipeData.put(new User("UserB", "", new ArrayList<>()), new HashMap<>());
        recipeData.put(new User("UserC", "", new ArrayList<>()), new HashMap<>());
        recipeData.put(new User("Екатерина", "89991213554", new ArrayList<>()), new HashMap<>());
        recipeData.put(new User("Василий", "89081334568", new ArrayList<>()), new HashMap<>());
        recipeData.put(new User("Иван", "89987745519", new ArrayList<>()), new HashMap<>());
    }

    @Override
    public Recipe fetchRecipe(String userId, String bookId) {
        User user = fetchUserByUserId(getCreatorId(bookId));
        Map<String, Recipe> userBooks = recipeData.get(user);

        if (!userBooks.containsKey(bookId)) {
            // У пользователя не найдена книга
            throw new NotFoundException();
        }
        return userBooks.get(bookId);
    }

    /* Нужно добавить:
        [-] удаление ингредиентов из холодильника участника
        [-] проверка на переполнение (countHave > countNeed)
     */
    @Override
    public List<MemberIngredients> addMember(String userId, String recipeId, MemberIngredients addedMemberIngredients) {
        User user = addedMemberIngredients.getUser();
        Recipe recipe = fetchRecipe(getCreatorId(recipeId), recipeId);
        MemberIngredients recipeMemberIngredients = recipe.getMembers().stream()
                .filter(mi -> mi.getUser().equals(user))
                .findAny()
                .orElse(null);
        if (recipeMemberIngredients == null) {                              // Если пользователь еще не участник рецепта, то заводим под него место в списке участников
            List<MemberIngredients> members = recipe.getMembers();
            members.add(new MemberIngredients(user, new ArrayList<>()));
            recipe.setMembers(members);
            recipeMemberIngredients = recipe.getMembers().stream()
                    .filter(mi -> mi.getUser().equals(user))
                    .findAny()
                    .orElseThrow(ExceptionInInitializerError::new);
        }
        String oldValue;
        for (AddedIngredient ai : addedMemberIngredients.getIngredients()) {
            boolean addedExisting = recipeMemberIngredients.getIngredients() != null && recipeMemberIngredients.getIngredients().stream()
                    .anyMatch(ingred -> ingred.getName().equals(ai.getName()));
            List<AddedIngredient> recipeIngredientList = recipeMemberIngredients.getIngredients();
            Ingredient updatedIngredient = recipe.getIngredients().stream()
                    .filter(ui -> ui.getName().equals(ai.getName()))
                    .findAny()
                    .orElseThrow(NotFoundException::new);
            if (Integer.valueOf(updatedIngredient.getCountHave()) + Integer.valueOf(ai.getCount()) > Integer.valueOf(updatedIngredient.getCountNeed())) {  //если закидываем больше countNeed
                Integer tmp = Integer.valueOf(updatedIngredient.getCountNeed()) - Integer.valueOf(updatedIngredient.getCountHave());
                if (tmp > 0)
                    ai.setCount(tmp.toString());
               /* else {
                    recipe.getMembers().
                }*/
                // здесь нужно допилить, чтобы правильно вычитались продукты из холодильника
            }
            if (!addedExisting) {                                           // Пользователь еще не добавлял этот ингредиент
                recipeIngredientList.add(ai);
                recipeMemberIngredients.setIngredients(recipeIngredientList);
                oldValue = "0";
            } else {                                                        // Пользователь уже добавлял этот ингредиент, нужно обновить поля
                AddedIngredient addedIngredient = recipeIngredientList.stream()
                        .filter(addedi -> addedi.getName().equals(ai.getName()))
                        .findAny()
                        .orElseThrow(RuntimeException::new);
                oldValue = addedIngredient.getCount();
                Integer value = Integer.valueOf(ai.getCount()) + Integer.valueOf(oldValue);
                ai.setCount(Integer.toString(value));
                recipeIngredientList.remove(addedIngredient);
                recipeIngredientList.add(ai);
                recipeMemberIngredients.setIngredients(recipeIngredientList);
            }
            updateValuesRecipeIngredients(recipe, oldValue, ai);
            updateStatus(recipe);
        }
        return recipe.getMembers();
    }

    /**
     * Внутри проверяет собраны ли все ингредиенты.
     * Если собраны, меняет статус рецепта на "Готовится",
     * иначе ничего не делает.
     *
     * @param recipe проверяемый рецепт
     */
    private void updateStatus(Recipe recipe) {
        boolean allIngredients = true;
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (!ingredient.getCountHave().equals(ingredient.getCountNeed())) {
                allIngredients = false;
                break;
            }
        }
        if (allIngredients)
            recipe.setStatus("Готовится");
        return;
    }

    /**
     * Обновляет поле countHave в рецепте recipe
     *
     * @param recipe   модифицируемый рецепт
     * @param oldValue старое значение поля countHave
     * @param ai       добавляемый ингредиент
     */
    private void updateValuesRecipeIngredients(Recipe recipe, String oldValue, AddedIngredient ai) {
        Integer value;
        Ingredient ingredient = recipe.getIngredients().stream()
                .filter(p -> p.getName().equals(ai.getName()))
                .findAny()
                .orElseThrow(NotFoundException::new);
        Integer addedValue = Integer.valueOf(ai.getCount()) - Integer.valueOf(oldValue);
        value = Integer.valueOf(ingredient.getCountHave()) + addedValue;
        ingredient.setCountHave(Integer.toString(value));
    }

    @Override
    public void deleteRecipe(String userId, String recipeId) {
        User user = fetchUserByUserId(userId);
        if (!recipeData.containsKey(user)) {
            // Пользователь не найден
            throw new NotFoundException();
        }
        if (!userId.equals(getCreatorId(recipeId))) {
            //Пользователь не является создателем рецепта
            throw new NotCreatorRecipeException();
        }
        Map<String, Recipe> userRecipes = recipeData.get(user);
        if (!userRecipes.containsKey(recipeId)) {
            // У пользователя не найден рецепт
            throw new NotFoundException();
        }
        userRecipes.remove(recipeId);
        recipeData.replace(user, userRecipes);
    }

    @Override
    public Recipe createRecipe(String userId, Recipe recipe) {  /* Добавление нового рецепта (complete) */
        User user = fetchUserByUserId(recipe.getCreator().getUserId());
        Map<String, Recipe> userBooks = recipeData.get(user);
        if (recipe.getMembers() == null)
            recipe.setMembers(new ArrayList<>());
        if (recipe.getCreator().getFridge() == null)
            recipe.getCreator().setFridge(new ArrayList<>());
        recipe.setId(Integer.toString(countRecipes.getAndIncrement()));
        userBooks.put(recipe.getId(), recipe);
        recipeCreatorMap.put(recipe.getId(), recipe.getCreator().getUserId());
        return recipe;
    }

    @Override
    public List<ShortRecipe> getAllShortRecipes() {
        List<ShortRecipe> list = new LinkedList<>();
        for (Map.Entry<User, Map<String, Recipe>> entry : recipeData.entrySet()) {
            for (Recipe r : entry.getValue().values()) {
                ShortRecipe sr = createShortRecipeByRecipe(r);
                list.add(sr);
            }
        }
        return list;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new LinkedList<>();
        for (Map.Entry<User, Map<String, Recipe>> entry : recipeData.entrySet()) {
            for (Recipe r : entry.getValue().values()) {
                list.add(r);
            }
        }
        return list;
    }


    @Override
    public void updateStatusFinally(String recipeId) {
        Recipe r = fetchRecipe(getCreatorId(recipeId), recipeId);
        r.setStatus("Завершено");
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

    public ShortRecipe createShortRecipeByRecipe(Recipe recipe) {
        return new ShortRecipe(recipe.getId(),
                recipe.getTitle(),
                (recipe.getDescription().length() > 50) ? (recipe.getDescription().substring(0, 50) + "...") : recipe.getDescription(),
                recipe.getStatus());
    }
//--------------------------------------FRIDGE---------------------------------------------


    @Override
    public List<AddedIngredient> getUserFridge(String userId) {
        User u = fetchUserByUserId(userId);
        return u.getFridge();
    }


    @Override
    public List<AddedIngredient> addIngredientInFridge(String userId, AddedIngredient newIng) {
        User u = fetchUserByUserId(userId);
        return u.addIngredientInFridge(newIng);
    }


    @Override
    public List<AddedIngredient> delIngredientFromFridge(String userId, AddedIngredient delIng) {
        User u = fetchUserByUserId(userId);
        return u.delIngredientInFridge(delIng);
    }

    @Override
    public void updateIngredientInFridge(String userId, AddedIngredient updatedIngredient) {
        User user = fetchUserByUserId(userId);
        user.updateIngredientInFridge(updatedIngredient);
    }

}
