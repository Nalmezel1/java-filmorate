package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;


    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/genres")
    public class GenreController {
        private final GenreService genreService;

        //Получить все жанры
        @GetMapping
        public Collection<Genre> getAllGenres() {
            return genreService.getAll();
        }

        //Получить жанры по id
        @GetMapping("/{id}")
        public Genre getGenreById(@PathVariable long id) {
            return genreService.getGenre(id);
        }

}
