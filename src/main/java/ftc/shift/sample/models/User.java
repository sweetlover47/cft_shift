package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

import java.util.Collection;
import java.util.List;

public class User {
    @ApiModelProperty(value = "Имя пользователя", required = true)
    private String name;

    @ApiModelProperty(value = "Уникальный идентификатор пользователя", required = true)
    private String userId;

    private List<AddedIngredient> fridge;

    public User() {
    }

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public User(String name, String userId, List<AddedIngredient> fridge) {
        this.name = name;
        this.userId = userId;
        this.fridge = fridge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            User user = (User) object;
            if (this.userId.equals(user.getUserId())) {
                result = true;
            }
        }
        return result;
    }

    //// fridge:
    public List<AddedIngredient> getFridge() {
        return fridge;
    }

    public void setFridge(List<AddedIngredient> fridge) {
        this.fridge = fridge;
    }

    public List<AddedIngredient> addIngredientInFridge(AddedIngredient newIngredient) {
        for (AddedIngredient ingredient : this.fridge) {
            if (ingredient.getName().equals(newIngredient.getName())) {
                ingredient.addCount(newIngredient.getCount());
                return this.fridge;
            }
        }

        this.fridge.add(newIngredient);
        return this.fridge;
    }

    public List<AddedIngredient> delIngredientInFridge(AddedIngredient delIngredient) {
        for (int i = 0; i < this.fridge.size(); i++) {
            if (this.fridge.get(i).getName().equals(delIngredient.getName())) {
                if (this.fridge.get(i).getCount().equals(delIngredient.getCount())) {
                    // delete whole ingredients from fridge
                    this.fridge.remove(i);
                    break;
                } else {
                    this.fridge.get(i).subCount(delIngredient.getCount());
                    break;
                }

            }

        }
        return this.fridge;
    }

    public void updateIngredientInFridge(AddedIngredient updatedIngredient) {
        for (AddedIngredient ingredient : this.fridge) {
            if (ingredient.getName().equals(updatedIngredient.getName())) {
                ingredient.setCount(updatedIngredient.getCount());
            }
        }
    }
}
