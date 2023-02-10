package ru.grechits.dailyjoke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.grechits.dailyjoke.Joke;
import ru.grechits.dailyjoke.JokeDto;
import ru.grechits.dailyjoke.JokeRepository;
import ru.grechits.dailyjoke.Status;
import ru.grechits.dailyjoke.exception.NotFoundException;
import ru.grechits.dailyjoke.exception.ValidationException;
import ru.grechits.dailyjoke.mapper.JokeMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;
import java.util.stream.Collectors;

import static ru.grechits.dailyjoke.mapper.DateTimeMapper.toLocalDateTime;
import static ru.grechits.dailyjoke.mapper.JokeMapper.toJoke;
import static ru.grechits.dailyjoke.mapper.JokeMapper.toJokeDto;

@Service
@RequiredArgsConstructor
public class JokeServiceImpl implements JokeService {
    private final JokeRepository repository;
    private final SplittableRandom random;

    //The three methods below perform the same function - they find a random joke, but they do it in different ways.
    @Override
    public JokeDto getRandom() {
        List<Long> publishedJokesIds = getPublishedJokesIds();
        int randomIndex = random.nextInt(0, publishedJokesIds.size());
        long id = publishedJokesIds.get(randomIndex);
        Joke joke = getJokeFromRepository(id);

        return toJokeDto(joke);
    }

    @Override
    public JokeDto getRandomUsingShuffle() {
        List<Long> publishedJokesIds = getPublishedJokesIds();
        Collections.shuffle(publishedJokesIds);
        Joke joke = getJokeFromRepository(publishedJokesIds.get(0));

        return toJokeDto(joke);
    }

    @Override
    public JokeDto getRandomUsingRecursion() {
        long jokesQuantity = repository.count();
        long randomId = random.nextLong(1, jokesQuantity);
        long id = getCorrectRandomId(randomId, jokesQuantity, random);
        Joke joke = getJokeFromRepository(id);

        return toJokeDto(joke);
    }

    @Override
    public JokeDto getById(long id) {
        Joke joke = getJokeFromRepository(id);

        return toJokeDto(joke);
    }

    @Override
    public List<JokeDto> getByParams(String start, String end, String status, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Joke> jokes = repository.findAllByCreatedBetweenAndStatus(
                start != null ? toLocalDateTime(start) : LocalDateTime.now().minusDays(1),
                end != null ? toLocalDateTime(end) : LocalDateTime.now(),
                status != null ? Status.valueOf(status) : Status.PUBLISHED, pageable);

        return jokes.stream()
                .map(JokeMapper::toJokeDto)
                .collect(Collectors.toList());
    }

    @Override
    public JokeDto create(JokeDto jokeDto) {
        Joke joke = repository.save(toJoke(jokeDto));

        return toJokeDto(joke);
    }

    @Override
    public JokeDto publish(long id) {
        Joke joke = getJokeFromRepository(id);
        checkStatus(joke);
        joke.setStatus(Status.PUBLISHED);
        Joke publishedJoke = repository.save(joke);

        return toJokeDto(publishedJoke);
    }

    @Override
    public JokeDto reject(long id) {
        Joke joke = getJokeFromRepository(id);
        checkStatus(joke);
        joke.setStatus(Status.REJECTED);
        Joke rejectedJoke = repository.save(joke);

        return toJokeDto(rejectedJoke);
    }

    @Override
    public void delete(long id) {
        getJokeFromRepository(id);
        repository.deleteById(id);
    }

    private List<Long> getPublishedJokesIds() {
        return repository.findAllByStatus(Status.PUBLISHED).stream()
                .map(Joke::getId)
                .collect(Collectors.toList());
    }

    private long getCorrectRandomId(long id, long jokesQuantity, SplittableRandom random) {
        Optional<Joke> randomJoke = repository.findByIdAndStatus(id, Status.PUBLISHED);
        if (randomJoke.isPresent()) {
            return randomJoke.get().getId();
        }
        long randomId = random.nextLong(1, jokesQuantity);
        getCorrectRandomId(randomId, jokesQuantity, random);
        return 0;
    }

    private Joke getJokeFromRepository(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Joke with ID = %d not found.", id)));
    }

    private void checkStatus(Joke joke) {
        if (!joke.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("The joke should be in the waiting state for publication.");
        }
    }
}
