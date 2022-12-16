package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.validation.Constraint;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film extends AbstractModel{
    @NotNull
    @NotBlank
    private String name;
    @Size(min = 1, message = "описание не может быть пустым")
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;

    private LocalDate releaseDate;
    @Positive(message = "родолжительность фильма должна быть положительной")
    private int duration;

    @JsonIgnore
    private Set<Long> likes = new HashSet<>();

    public void addLike(Long userId) {
        likes.add(userId);
    }
    public void removeLike(Long userId) {
        likes.remove(userId);
    }

}
