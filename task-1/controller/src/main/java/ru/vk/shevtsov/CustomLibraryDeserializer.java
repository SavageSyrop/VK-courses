package ru.vk.shevtsov;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class CustomLibraryDeserializer implements JsonDeserializer<Library> {
    private static final DateTimeFormatter fullDateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public Library deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray jsonAuthors = jsonObject.getAsJsonArray("authors");
        HashMap<Long, Author> authorHashMap = new HashMap<>();

        for (JsonElement jsonElement : jsonAuthors) {
            JsonObject jsonAuthor = jsonElement.getAsJsonObject();
            Long authorId = jsonAuthor.get("id").getAsLong();
            LocalDate authorBornDate = LocalDate.parse(jsonAuthor.get("bornDate").getAsString(), fullDateFormat);
            LocalDate authorDeathDate = LocalDate.parse(jsonAuthor.get("deathDate").getAsString(), fullDateFormat);
            Author author = new Author(authorId, jsonAuthor.get("name").getAsString(), authorBornDate, authorDeathDate);
            authorHashMap.put(authorId, author);
        }

        JsonArray jsonBooks = jsonObject.getAsJsonArray("books");
        HashMap<Long, Book> bookHashMap = new HashMap<>();
        for (JsonElement jsonElement : jsonBooks) {
            JsonObject jsonBook = jsonElement.getAsJsonObject();
            Author author = authorHashMap.get(jsonBook.get("authorId").getAsLong());
            if (author == null) {
                continue;
            }
            Long bookId = jsonBook.get("id").getAsLong();
            String name = jsonBook.get("name").getAsString();
            Integer firstPublishedYear = jsonBook.get("firstPublishedYear").getAsInt();
            String language = jsonBook.get("language").getAsString();
            Book book = new Book(bookId, name, author, firstPublishedYear, language);
            bookHashMap.put(bookId, book);
        }
        return new Library(bookHashMap);
    }
}
