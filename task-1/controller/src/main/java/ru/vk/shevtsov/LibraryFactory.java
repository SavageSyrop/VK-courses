package ru.vk.shevtsov;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.FileReader;


public class LibraryFactory {
    @Getter
    private Library library;

    public LibraryFactory(Gson gson) throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader("./library.txt"));
        this.library = gson.fromJson(reader, Library.class);
    }
}
