package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User  extends AbstractModel{


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
    Set<Long> friends = new HashSet<>();
    public void addFriend(Long id) {
        friends.add(id);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }
}
