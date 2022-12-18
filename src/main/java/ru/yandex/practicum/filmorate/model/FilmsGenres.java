package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmsGenres {

    private long idFilm;
    private int genreId;
    private String name;
}