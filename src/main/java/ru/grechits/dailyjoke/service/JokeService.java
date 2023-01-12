package ru.grechits.dailyjoke.service;

import ru.grechits.dailyjoke.JokeDto;

import java.util.List;

public interface JokeService {

    JokeDto getRandom();

    JokeDto getById(long id);

    List<JokeDto> getByParams(String start, String end, String status, int from, int size);

    JokeDto create(JokeDto jokeDto);

    JokeDto publish(long id);

    JokeDto reject(long id);

    void delete(long id);
}
