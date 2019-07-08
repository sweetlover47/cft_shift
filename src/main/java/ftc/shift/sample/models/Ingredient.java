package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

public class Ingredient {
    @ApiModelProperty(value = "Название ингредиента", required = true)
    private String name;

    @ApiModelProperty(value = "Имеется", required = true)
    private Integer collected;

    @ApiModelProperty(value = "Необходимо", required = true)
    private Integer required;

    public Ingredient() {}

    public Ingredient(String name, Integer collected, Integer required) {
        this.name = name;
        this.collected = collected;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCollected() {
        return collected;
    }

    public void setCollected(Integer collected) {
        this.collected = collected;
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }
}
