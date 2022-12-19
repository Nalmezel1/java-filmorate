package ru.yandex.practicum.filmorate.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friends {
    private int friendsId;
    private long userId;
    private long friendId;
}
