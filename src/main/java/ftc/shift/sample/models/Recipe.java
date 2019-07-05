package ftc.shift.sample.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

public class Recipe extends ShortRecipe {
    @ApiModelProperty(value = "Телефон создателя", required = true)
    private String creator;

    @ApiModelProperty(value = "Ингредиенты рецепта", required = true)
    private List<String> ingredients;

    @ApiModelProperty(value = "Участники рецепта", required = true)
    private List<User> members;

    public Recipe() {
    }

    public Recipe(String id, String title, String creator, String description, int status, List<String> ingredients, List<User> members) {
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public int getStatus() { return status; }

    public void setStatus(int status) { this.status = status; }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}