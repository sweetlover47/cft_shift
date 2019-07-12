package ftc.shift.sample.api;


import ftc.shift.sample.models.*;
import ftc.shift.sample.services.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Collection;
import java.util.List;

/**
 * Все аннотации, начинающиеся с @Api нужны только для построения <a href="http://localhost:8081/swagger-ui.html#/">swagger-документации</a>
 * Их удаление ничего не сломает.
 */
@RestController
@Api(description = "Запросы для работы с рецептами")
public class RecipesController {

    private static final String RECIPE_PATH = "/api/v001/recipe";
    private static final String ALL_RECIPES_PATH = "/api/v001/recipes";
    private static final String FRIDGE_PATH = "/api/v001/fridge";

    @Autowired
    private RecipeService service;

    @PostMapping(RECIPE_PATH)      /* Добавление нового рецепта */
    @ApiOperation(value = "Добавление нового рецепта")
    public ResponseEntity<Recipe> createRecipe(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestHeader("userId") String userId,
            @ApiParam(value = "Данные для нового рецепта")
            @RequestBody Recipe recipe) {
        Recipe result = service.createRecipe(userId, recipe);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping(RECIPE_PATH + "/{recipeId}")     /* Получение конкретного рецепта (переход на страницу рецепта) */
    @ApiOperation(value = "Получение рецепта по его идентификатору (переход на страницу рецепта)")
    public ResponseEntity<Recipe> readRecipe(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestHeader("userId") String userId,
            @ApiParam(value = "Идентификатор рецепта")
            @PathVariable String recipeId) {
        /* Нужно получить идентификатор создателя через рецепт, который известен по идентификатору */
        String creatorId = service.getCreatorId(recipeId);
        Recipe recipe = service.provideRecipe(creatorId, recipeId);
        return ResponseEntity.ok(recipe);
    }

    @PutMapping(RECIPE_PATH + "/{recipeId}")   /* Добавление участника в список */
    @ApiOperation(value = "Добавление нового участника в список участников конкретного рецепта")
    public ResponseEntity<List<MemberIngredients>> addMember(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestHeader("userId") String userId,
            @ApiParam(value = "Идентификатор рецепта")
            @PathVariable String recipeId,
            @ApiParam(value = "Добавленные ингредиенты")
            @RequestBody MemberIngredients memberIngredients) {
        List<MemberIngredients> resultMemberIngredients = service.addMemberToRecipe(userId, recipeId, memberIngredients);
        return ResponseEntity.ok(resultMemberIngredients);
    }

    @PutMapping(RECIPE_PATH + "/{recipeId}/status")
    @ApiOperation(value = "Изменение статуса рецепта на \"Готово\"")
    public ResponseEntity<?> updateStatus(
            @ApiParam(value = "Идентификатор рецепта")
            @PathVariable String recipeId ){
        service.updateStatus(recipeId);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping(RECIPE_PATH + "/{recipeId}")  /* Удалить рецепт */
    @ApiOperation(value = "Удаление существующего рецепта")
    public ResponseEntity<?> deleteRecipe(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestHeader("userId") String userId,
            @ApiParam(value = "Идентификатор книги, которую необходимо удалить")
            @PathVariable String recipeId) {
        service.deleteRecipe(userId, recipeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(ALL_RECIPES_PATH)
    @ApiOperation(value = "Получение списка всех рецептов")
    public ResponseEntity<Collection<ShortRecipe>> listRecipes(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestHeader("userId") String userId) {
        Collection<ShortRecipe> recipes = service.provideRecipes(userId);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping(RECIPE_PATH + "/find") // Поиск рецепта по имени
    @ApiOperation(value = "Поиск рецепта по названию")
    public ResponseEntity<Collection<ShortRecipe>> findRecipesByTitle(
            @ApiParam(value = "Фильтр для рецепта")
            @RequestParam("search") String recipeFilter) {
        Collection<ShortRecipe> findedRecipes = service.findRecipes(recipeFilter);
        return ResponseEntity.ok(findedRecipes);
    }


    //-----------------------------------------------------------------------------------------------------------

    @GetMapping(FRIDGE_PATH) // Получение холодильника
    @ApiOperation(value = "Получение холодильника пользователя")
    public ResponseEntity<Collection<AddedIngredient>> sendFridge(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestHeader("userId") String userId) {
        Collection<AddedIngredient> fridge = service.getFridge(userId);
        return ResponseEntity.ok(fridge);
    }

    @PutMapping(FRIDGE_PATH)
    @ApiOperation(value = "Изменение количества ингредиента в холодильнике")
    public ResponseEntity<?> updateIngredientInFridge(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestHeader("userId") String userId,
            @ApiParam(value = "Добавленный ингредиент")
            @RequestBody AddedIngredient addedIngredient) {
        service.updateIngredientInFridge(userId, addedIngredient);
        return ResponseEntity.ok().build();
    }



    @PutMapping(FRIDGE_PATH + "/add") // Добавление игредиента в холодильник
    @ApiOperation(value = " Добавление ингредиента в холодильник")
    public ResponseEntity<Collection<AddedIngredient>> addIngredientInFridge(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestHeader("userId") String userId,
            @ApiParam(value = "Добавленный ингредиент")
            @RequestBody AddedIngredient addedIngredient) {
        Collection<AddedIngredient> fridge = service.addIngredientInFridge(userId, addedIngredient);
        return ResponseEntity.ok(fridge);
    }


    @PutMapping(FRIDGE_PATH + "/del") // Удаление ингредиента из холодилника
    @ApiOperation(value = "Удаление ингредиента из холодилника")
    public ResponseEntity<Collection<AddedIngredient>> delIngredientFromFridge(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestHeader("userId") String userId,
            @ApiParam(value = "Удаляемый ингредиент")
            @RequestBody AddedIngredient delIngredient) {


        Collection<AddedIngredient> fridge = service.delIngredientFromFridge(userId, delIngredient);
        return ResponseEntity.ok(fridge);
    }

}