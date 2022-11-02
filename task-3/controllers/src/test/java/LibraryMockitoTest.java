import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.vk.Book;
import ru.vk.Library;
import ru.vk.exceptions.EmptyShelfException;
import ru.vk.exceptions.ShelfTooSmallException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

class LibraryMockitoTest extends AbstractTest {

    @BeforeEach
    public void mockBookList() {
        Mockito.when(mockedFileBookFactory.books()).thenReturn(books.values());
        System.setOut(systemOut);
    }

    @BeforeEach
    public void mockLibrary() {
        doAnswer(invocation -> {
            Integer capacity = invocation.getArgument(0);
            return new Library(capacity, mockedFileBookFactory);
        }).when(mockedLibraryFactory).library(any(Integer.class));
    }


    @Test
    public void whenCapacityIsLesserThanReceivedBookCountThenThrowException() {
        assertThrows(ShelfTooSmallException.class, () -> mockedLibraryFactory.library(books.size() - 1));
    }

    @Test
    public void checkBooksOrderEqualToFile() {
        Library libraryWithOneEmptyShell = mockedLibraryFactory.library(books.size() + 1);
        for (int i = 0; i < books.size(); i++) {
            assertEquals(books.get(i), libraryWithOneEmptyShell.getBooks().get(i));
        }
        assertNull(libraryWithOneEmptyShell.getBooks().get(books.size() + 1));
        assertNull(books.get(books.size() + 1));
    }


    @Test
    public void takenBookAndShelfIndexInfoPrintsInConsole() {
        String line = new GsonBuilder().setPrettyPrinting().create().toJson(books.get(0)) + "\n Was at book shell with index 0";
        doAnswer(invocation -> {
            String str = invocation.getArgument(0);
            assertEquals(str, line);
            return null;
        }).when(systemOut).println(any(String.class));
        mockedLibraryFactory.library(books.size()).takeBook(0);
    }

    @Test
    public void takenBookReturnsCorrectlyAndShelfBecomesEmpty() {
        Book book = books.get(0);
        Library library = mockedLibraryFactory.library(books.size());
        assertEquals(library.takeBook(0), book);
        assertThrows(EmptyShelfException.class, () -> library.takeBook(0));
    }


    @Test
    public void testAllBooksFromShelfsPrintedInConsole() {
        String line = new GsonBuilder().setPrettyPrinting().create().toJson(books);

        doAnswer(invocation -> {
            String str = invocation.getArgument(0);
            assertEquals(str, line);
            return null;
        }).when(systemOut).println(any(String.class));
        mockedLibraryFactory.library(books.size()).printBooks();
    }
}