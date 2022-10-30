import com.google.inject.Inject;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vk.*;

import java.io.PrintStream;
import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
public class AbstractTest {
    @Getter
    protected HashMap<Integer, Book> mockedBooks = new HashMap<>();

    @Inject
    protected BooksFactory injectedBookFactory;

    @Mock
    protected FileBookFactory mockedFileBookFactory;

    @Mock
    protected LibraryFactory mockedLibraryFactory;

    @Mock
    protected PrintStream systemOut;

    @BeforeEach
    public void fillMap() {
        Author dost = new Author(1L, "Fedor Dostoevsky");
        Author lev = new Author(2L, "Lev Tolstoy");
        Author marg = new Author(3L, "Margaret Mitchell");
        mockedBooks.put(0, new Book(1L, "Crime And Punishment", dost));
        mockedBooks.put(1, new Book(2L, "War and Peace", lev));
        mockedBooks.put(2, new Book(3L, "Gone with the wind", marg));
        mockedBooks.put(3, new Book(4L, "Anna Karenina", lev));
        mockedBooks.put(4, new Book(5L, "Humiliated and Insulted", dost));
    }
}
