import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.vk.Book;
import ru.vk.Library;
import ru.vk.exceptions.ShellTooSmallException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

class LibraryMockitoTest extends AbstractTest {

    @BeforeEach
    public void mockBookList() {
        Mockito.when(mockedFileBookFactory.books()).thenReturn(mockedBooks.values());
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
    public void library_WhenCapacityLessThanBookCountInFile_ThenThrowsShellTooSmallException() {
        assertThrows(ShellTooSmallException.class, () -> mockedLibraryFactory.library(mockedBooks.size() - 1));
    }

    @Test
    public void library_WhenCapacityIsGreaterThanBookCountInFile_ThenSortedAsInFileAndHasEmptyShells() {
        Library libraryWithOneEmptyShell = mockedLibraryFactory.library(mockedBooks.size() + 1);
        for (int i = 0; i < mockedBooks.size(); i++) {
            assertEquals(mockedBooks.get(i), libraryWithOneEmptyShell.getBooks().get(i));
        }
        assertNull(libraryWithOneEmptyShell.getBooks().get(mockedBooks.size() + 1));
        assertNull(mockedBooks.get(mockedBooks.size() + 1));
    }


    @Test
    public void takeBook_WhenShellContainsBookByIndex_ThenPrintInConsole() {
        String line = new GsonBuilder().setPrettyPrinting().create().toJson(mockedBooks.get(0)) + "\n Was at book shell with index 0";
        doAnswer(invocation -> {
            String str = invocation.getArgument(0);
            assertEquals(str, line);
            return null;
        }).when(systemOut).println(any(String.class));
        mockedLibraryFactory.library(mockedBooks.size()).takeBook(0);
    }

    @Test
    public void takeBook_WhennShellContainsBookByIndex_ThenReturnThatBook() {
        Book book = mockedBooks.get(0);
        assertEquals(mockedLibraryFactory.library(mockedBooks.size()).takeBook(0), book);
    }


    @Test
    public void printBooks_WhenCorrectData_ThenPrintInConsole() {
        String line = new GsonBuilder().setPrettyPrinting().create().toJson(mockedBooks);

        doAnswer(invocation -> {
            String str = invocation.getArgument(0);
            assertEquals(str, line);
            return null;
        }).when(systemOut).println(any(String.class));
        mockedLibraryFactory.library(mockedBooks.size()).printBooks();
    }
}