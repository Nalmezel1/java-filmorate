package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractModel;


import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
*  Абстрактный класс для доп задания
* */

@Slf4j
@RestController
public abstract class AbstractController<T extends AbstractModel> {}
