package ru.aliev.bookproject.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {

    private int id;

    private String title;

    private String author;

    private int publicationYear;
}
