package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

public class Ingredient {
    @ApiModelProperty(value = "Название ингредиента", required = true)
    private String name;

    @ApiModelProperty(value = "Имеется", required = true)
    private String countHave;

    @ApiModelProperty(value = "Необходимо", required = true)
    private String countNeed;

    public Ingredient() {
    }

    public Ingredient(String name, String countHave, String countNeed) {
        this.name = name;
        this.countHave = countHave;
        this.countNeed = countNeed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountHave() {
        return countHave;
    }

    public void setCountHave(String countHave) {
        this.countHave = countHave;
    }

    public String getCountNeed() {
        return countNeed;
    }

    public void setCountNeed(String countNeed) {
        this.countNeed = countNeed;
    }
}
