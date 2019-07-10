package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class Recipe extends ShortRecipe {
    @ApiModelProperty(value = "Телефон создателя", required = true)
    private User creator;

    @ApiModelProperty(value = "Ингредиенты рецепта", required = true)
    private List<Ingredient> ingredients;

    @ApiModelProperty(value = "Участники рецепта", required = true)
    // private List<User> members;
    private List<MemberIngredients> members;

    public Recipe() {
    }

    public Recipe(String id, String title, User creator, String description, String status, List<Ingredient> ingredients, List<MemberIngredients> members) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.description = description;
        this.status = status;
        this.ingredients = ingredients;
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MemberIngredients> getMembers() {
        return members;
    }

    public void setMembers(List<MemberIngredients> members) {
        this.members = members;
    }
}