package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

public class ShortRecipe {
    @ApiModelProperty(value = "Уникальный идентификатор рецепта", required = true)
    protected String id;

    @ApiModelProperty(value = "Название рецепта", required = true)
    protected String title;

    @ApiModelProperty(value = "Описание рецепта", required = true)
    protected String description;

    @ApiModelProperty(value = "Статус рецепта", required = true)
    protected String status;

    public ShortRecipe() {
    }

    public ShortRecipe(String id, String title, String description, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

}
