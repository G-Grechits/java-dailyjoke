package ru.grechits.dailyjoke.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.grechits.dailyjoke.JokeDto;
import ru.grechits.dailyjoke.service.JokeService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserJokeController {
    private final JokeService service;

    @GetMapping("/jokes")
    public JokeDto getRandomJoke() {
        JokeDto joke = service.getRandom();
        log.info("A GET request was made at /users/jokes");

        return joke;
    }

    @PostMapping("/joke")
    public JokeDto createJoke(@RequestBody @Valid JokeDto jokeDto) {
        JokeDto joke = service.create(jokeDto);
        log.info("A POST request was made at /users/joke");

        return joke;
    }
}
