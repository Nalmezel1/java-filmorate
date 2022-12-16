package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.Constraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film extends AbstractModel{


    @NotNull
    @NotEmpty(message = "название не может быть пустым")
    private String name;
    @Size(min = 1, message = "описание не может быть пустым")
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;

    private LocalDate releaseDate;
    @Positive(message = "родолжительность фильма должна быть положительной")
    private int duration;
}
