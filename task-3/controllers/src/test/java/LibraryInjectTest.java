import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.vk.*;
import ru.vk.exceptions.EmptyShellException;
import ru.vk.exceptions.ShellTooSmallException;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryInjectTest extends AbstractTest {

    private LibraryFactory libraryFactory;

    @BeforeEach
    public void initializeBooks() throws URISyntaxException {
        String filePath = Paths.get(LibraryInjectTest.class.getResource("/books.txt").toURI()).toString();
        final Injector injector = Guice.createInjector(new ConfigModule(filePath));
        injector.injectMembers(this);
        libraryFactory = injector.getInstance(LibraryFactory.class);
    }

    @Test
    public void takeBook_WhenBookShellIsEmpty_ThenThrowsEmptyShellException() {
        Library library = libraryFactory.library(injectedBookFactory.books().size() + 1);
        assertThrows(EmptyShellException.class, () -> library.takeBook(injectedBookFactory.books().size() + 1));
    }

    @Test
    public void putBook_WhenFirstEmptyShellFound_ThenPutBookInThisShell() {
        Library library = libraryFactory.library(injectedBookFactory.books().size() + 1);
        library.takeBook(2);
        Book bookFromShellThree = library.takeBook(3);
        assertNull(library.getBooks().get(2));
        assertNull(library.getBooks().get(3));
        library.putBook(bookFromShellThree);
        assertNotNull(library.getBooks().get(2));
        assertNull(library.getBooks().get(3));
    }

    @Test
    public void putBook_WhenNoEmptyShells_ThenThrowsShellTooSmallException() {
        Author kanyeWest = new Author(4L, "Kanye West");
        Library library = libraryFactory.library(injectedBookFactory.books().size());
        assertThrows(ShellTooSmallException.class, () -> library.putBook(new Book(6L, "Genius", kanyeWest)));
    }
}
