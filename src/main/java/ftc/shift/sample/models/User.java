package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

import java.util.Collection;

public class User {
    @ApiModelProperty(value = "Имя пользователя", required = true)
    private String name;

    @ApiModelProperty(value = "Уникальный идентификатор пользователя", required = true)
    private String userId;

    //private Fridge fridge;

    public User() {}

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
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
    public boolean equals (Object object) {
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
}
