package ru.vk;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public final class FileBookFactory implements BooksFactory {

    @NotNull
    private static final Type listBooksType = new TypeToken<ArrayList<Book>>() {
    }.getType();

    @NotNull
    private final String fileName;

    public FileBookFactory(@NotNull String fileName) {
        this.fileName = fileName;
    }

    @NotNull
    @Override
    public Collection<Book> books() {
        try {
            return new Gson().fromJson(new BufferedReader(new FileReader(fileName)), listBooksType);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
