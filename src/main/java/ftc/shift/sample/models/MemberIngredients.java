package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class MemberIngredients {

    @ApiModelProperty(value = "Пользователь", required = true)
    private User user;

    @ApiModelProperty(value = "Ингредиенты", required = true)
    private List<AddedIngredient> ingredients;

    public MemberIngredients() {
    }

    public MemberIngredients(User user, List<AddedIngredient> ingredients) {
        this.user = user;
        this.ingredients = ingredients;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<AddedIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<AddedIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
