package ru.grechits.dailyjoke.mapper;

import ru.grechits.dailyjoke.Joke;
import ru.grechits.dailyjoke.JokeDto;
import ru.grechits.dailyjoke.Status;

import java.time.LocalDateTime;

import static ru.grechits.dailyjoke.mapper.DateTimeMapper.toTextDateTime;

public class JokeMapper {

    public static Joke toJoke(JokeDto jokeDto) {
        return new Joke(null, jokeDto.getText(), LocalDateTime.now(), Status.WAITING);
    }

    public static JokeDto toJokeDto(Joke joke) {
        return new JokeDto(joke.getId(), joke.getText(), toTextDateTime(joke.getCreated()), joke.getStatus().name());
    }
}
