package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class Recipe {
  @ApiModelProperty(value = "Уникальный идентификатор рецепта", required = true)
  private String id;

  @ApiModelProperty(value = "Название рецепта", required = true)
  private String title;

  @ApiModelProperty(value = "Телефон создателя", required = true)
  private String creator;

  @ApiModelProperty(value = "Описание рецепта", required = true)
  private String description;

  @ApiModelProperty(value = "Статус рецепта", required = true)
  private int status;

  @ApiModelProperty(value = "Ингредиенты рецепта", required = true)
  private List<String> ingredients;

  public Recipe() {
  }

  public Recipe(String id, String title, String creator, String description, int status, List<String> ingredients) {
    this.id = id;
    this.title = title;
    this.creator = creator;
    this.description = description;
    this.status = status;
    this.ingredients = ingredients;
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
}
