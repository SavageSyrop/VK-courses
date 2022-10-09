package ru.vk.shevtsov;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.time.LocalDate;


public class App {
    public static void main(String[] args) throws FileNotFoundException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Library.class, new CustomLibraryDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        LibraryFactory libraryFactory = new LibraryFactory(gson);
        Library library = libraryFactory.getLibrary();
        System.out.println(gson.toJson(library.getBooksByAuthor(args[0])));
    }
}
