package ru.vk;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        Injector injector = Guice.createInjector(new ConfigModule(Paths.get(Main.class.getResource(args[1]).toURI()).toString()));
        Library library = injector.getInstance(LibraryFactory.class).library(Integer.parseInt(args[0]));
        library.printBooks();
    }
}