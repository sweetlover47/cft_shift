package ftc.shift.sample.api;


import ftc.shift.sample.models.Recipe;
import ftc.shift.sample.models.ShortRecipe;
import ftc.shift.sample.models.User;
import ftc.shift.sample.services.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private RecipeService service;

    @PostMapping(RECIPE_PATH)      /* Добавление нового рецепта */
    @ApiOperation(value = "Добавление нового рецепта")
    public ResponseEntity<Recipe> createRecipe(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestHeader("userId") String userId,
            @ApiParam(value = "Данные для нового рецепта (ID, название, телефон создателя, описание, ингредиенты)")
            @RequestBody Recipe recipe) {
        Recipe result = service.createRecipe(userId, recipe);
        return ResponseEntity.ok(result);   /* Возможно, не надо ничего возвращать ?????????? */
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
    public ResponseEntity<List<String>> addMember(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestHeader("userId") String userId,
            @ApiParam(value = "Идентификатор рецепта")
            @PathVariable String recipeId) {
        List<String> members = service.addMemberToRecipe(userId, recipeId);
        return ResponseEntity.ok(members);
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

    @GetMapping(ALL_RECIPES_PATH)                   /* Получить список всех рецептов (complete) */
    @ApiOperation(value = "Получение списка всех рецептов")
    public ResponseEntity<Collection<ShortRecipe>> listRecipes(
            @ApiParam(value = "Идентификатор пользователя")
            @RequestHeader("userId") String userId) {
        Collection<ShortRecipe> recipes = service.provideRecipes(userId);
        return ResponseEntity.ok(recipes);
    }
}