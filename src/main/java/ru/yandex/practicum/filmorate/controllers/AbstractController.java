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
public abstract class AbstractController<T extends AbstractModel> {
    private Map<Long, T> сontrollerMap = new HashMap<>();
    private long currentMaxId = 0L;


    @PostMapping
    public T create(@Valid @RequestBody T item) throws ValidationException {

        validate(item);
        currentMaxId++;
        item.setId(currentMaxId);

        сontrollerMap.put(item.getId(), item);

        log.info(String.format("Элемент создан", item.getId()));
        log.debug(String.format("info = %s", item.toString()));
        return item;
    }

    @PutMapping
    public T update (@Valid @RequestBody T item) throws ValidationException{
        validate(item);
        if (!сontrollerMap.containsKey(item.getId())) {
            throw new ValidationException(String.format("Элемент с id= %d не найден", item.getId()));
        }
        сontrollerMap.put(item.getId(), item);
        log.info(String.format("Элемент обновлен", item.getId()));
        log.debug(String.format("info = %s", item.toString()));
        return item;
    }

    @GetMapping
    public List<T> getAll() {
        return List.copyOf(сontrollerMap.values());
    }

    public void validate(T item) throws ValidationException{}
}
