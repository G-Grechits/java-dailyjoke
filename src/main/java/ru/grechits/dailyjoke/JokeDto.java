package ru.grechits.dailyjoke;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class JokeDto {
    private Long id;

    @NotBlank
    @Length(min = 30, max = 300)
    private String text;

    private String created;
    private String status;
}
