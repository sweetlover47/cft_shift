package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

public class AddedIngredient {
    @ApiModelProperty(value = "Название ингредиента", required = true)
    private String name;

    @ApiModelProperty(value = "Количество", required = true)
    private String count;

    public AddedIngredient() {
    }


    public AddedIngredient(String name, String count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof AddedIngredient) {
            AddedIngredient ptr = (AddedIngredient) v;
            retVal = this.getName() == ptr.getName();
        }
        return retVal;
    }
}
