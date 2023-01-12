package ru.grechits.dailyjoke.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.grechits.dailyjoke.JokeDto;
import ru.grechits.dailyjoke.service.JokeService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminJokeController {
    private final JokeService service;

    @GetMapping("/joke/{jokeId}")
    public JokeDto getJokeById(@PathVariable @Positive long jokeId) {
        JokeDto joke = service.getById(jokeId);
        log.info("A GET request was made at /admin/joke/" + jokeId);

        return joke;
    }

    @GetMapping("/jokes")
    public List<JokeDto> getJokesByParams(@RequestParam(required = false) String start,
                                          @RequestParam(required = false) String end,
                                          @RequestParam(required = false) String status,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(defaultValue = "10") @Positive int size) {
        List<JokeDto> jokes = service.getByParams(start, end, status, from, size);
        log.info("A GET request was made at /admin/jokes");

        return jokes;
    }

    @PatchMapping("/joke/{jokeId}/publish")
    public JokeDto publishJoke(@PathVariable @Positive long jokeId) {
        JokeDto joke = service.publish(jokeId);
        log.info("A PATCH request was made at /admin/joke/{}/publish", jokeId);

        return joke;
    }

    @PatchMapping("/joke/{jokeId}/reject")
    public JokeDto rejectJoke(@PathVariable @Positive long jokeId) {
        JokeDto joke = service.reject(jokeId);
        log.info("A PATCH request was made at /admin/joke/{}/reject", jokeId);

        return joke;
    }

    @DeleteMapping("/joke/{jokeId}")
    public void deleteJoke(@PathVariable @Positive long jokeId) {
        service.delete(jokeId);
        log.info("A DELETE request was made at /admin/joke/" + jokeId);
    }
}
