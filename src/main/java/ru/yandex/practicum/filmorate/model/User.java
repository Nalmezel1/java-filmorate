package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;


    @NotNull(message = "электронная почта не может быть пустой")
    @Email(message = "электронная почта должна содержать символ @")
    @NotBlank
    private String email;
    @NotNull
    @NotEmpty(message = "логин не может быть пустым")
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @Past(message = "дата рождения не может быть в будущем")
    private LocalDate birthday;

}
