package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

import java.util.Collection;

public class User {
    @ApiModelProperty(value = "Уникальный идентификатор пользователя", required = true)
    private String userId;

    @ApiModelProperty(value = "Список рецептов, созданных пользователем", required = true)
    private Collection<Recipe> userRecipes;

    //private Fridge fridge;

    public User() {}

    public User(String userId, Collection<Recipe> userRecipes) {
        this.userId = userId;
        this.userRecipes = userRecipes;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Collection<Recipe> getUserRecipes() {
        return userRecipes;
    }

    public void setUserRecipes(Collection<Recipe> userRecipes) {
        this.userRecipes = userRecipes;
    }

    @Override
    public boolean equals (Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            User user = (User) object;
            if (this.userId == user.getUserId()) {
                result = true;
            }
        }
        return result;
    }
}
