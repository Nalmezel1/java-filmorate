package ru.yandex.practicum.filmorate.model;
import lombok.*;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Likes {
        private int likeId;
        private int filmId;
        private int userId;

}
