package ru.grechits.dailyjoke;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JokeRepository extends JpaRepository<Joke, Long> {

    Optional<Joke> findByIdAndStatus(long id, Status status);

    List<Joke> findAllByCreatedBetweenAndStatus(LocalDateTime start, LocalDateTime end, Status status, Pageable pageable);
}
