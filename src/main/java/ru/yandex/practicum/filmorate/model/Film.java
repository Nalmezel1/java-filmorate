package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Constraint;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Film{
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @Size(min = 1, message = "описание не может быть пустым")
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;

    private LocalDate releaseDate;
    @Positive(message = "родолжительность фильма должна быть положительной")
    private int duration;

    private int rate;
    private List<Genre> genres;
    private Mpa mpa;


}
