package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User  extends AbstractModel{


    @NotNull(message = "электронная почта не может быть пустой")
    @Email(message = "электронная почта должна содержать символ @")
    private String email;
    @NotNull
    @NotEmpty(message = "логин не может быть пустым")
    private String login;
    private String name;
    @NotNull
    @Past(message = "дата рождения не может быть в будущем")
    private LocalDate birthday;
}
