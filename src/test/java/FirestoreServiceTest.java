import com.example.warehouse.model.Book;
import com.example.warehouse.model.User;
import com.example.warehouse.service.FirestoreService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.concurrent.ExecutionException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FirestoreServiceTest {

    @Mock
    private Firestore db;

    @Mock
    private CollectionReference booksCollection;

    @Mock
    private DocumentReference bookDocument;

    @Mock
    private ApiFuture<DocumentSnapshot> documentSnapshotApiFuture;

    private FirestoreService firestoreService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        firestoreService = new FirestoreService();
        firestoreService.db = db;  // Mock the Firestore instance
    }

    @Test
    public void testCreateBook() throws ExecutionException, InterruptedException {
        // Arrange
        Book book = new Book("Book Title", "Author", "2024", "12345", 29.99, 10, "Description", "http://example.com", "Fiction");
        when(db.collection("book")).thenReturn(booksCollection);
        when(booksCollection.add(anyMap())).thenReturn(mock(DocumentReference.class));

        // Act
        firestoreService.createBook(book);

        // Assert
        verify(booksCollection).add(anyMap());  // Verify if Firestore add method was called
    }

    @Test
    public void testGetBookById() throws ExecutionException, InterruptedException {
        // Arrange
        String bookId = "123";
        Book expectedBook = new Book("Book Title", "Author", "2024", "12345", 29.99, 10, "Description", "http://example.com", "Fiction", bookId);

        when(db.collection("book")).thenReturn(booksCollection);
        when(booksCollection.document(bookId)).thenReturn(bookDocument);
        when(bookDocument.get()).thenReturn(documentSnapshotApiFuture);
        DocumentSnapshot documentSnapshot = mock(DocumentSnapshot.class);
        when(documentSnapshot.exists()).thenReturn(true);
        when(documentSnapshot.getData()).thenReturn(mock(Map.class));

        // Act
        Book actualBook = firestoreService.getBookById(bookId);

        // Assert
        assertNotNull(actualBook);
        assertEquals(expectedBook.getId(), actualBook.getId());
    }

    @Test
    public void testGetAllBooks() throws ExecutionException, InterruptedException {
        // Arrange
        Book book1 = new Book("Book Title", "Author", "2024", "12345", 29.99, 10, "Description", "http://example.com", "Fiction");
        Book book2 = new Book("Another Book", "Author", "2024", "67890", 39.99, 5, "Description", "http://example.com", "Non-fiction");

        when(db.collection("book")).thenReturn(booksCollection);
        when(booksCollection.get()).thenReturn(mock(ApiFuture.class));
        QuerySnapshot querySnapshot = mock(QuerySnapshot.class);
        when(querySnapshot.getDocuments()).thenReturn(List.of(mock(QueryDocumentSnapshot.class)));

        // Act
        List<Book> books = firestoreService.getAllBooks();

        // Assert
        assertFalse(books.isEmpty());
    }

    @Test
    public void testUpdateBook() throws ExecutionException, InterruptedException {
        // Arrange
        String bookId = "123";
        Book updatedBook = new Book("Updated Title", "New Author", "2025", "54321", 49.99, 15, "Updated Description", "http://example.com", "Fantasy");
        when(db.collection("book")).thenReturn(booksCollection);
        when(booksCollection.document(bookId)).thenReturn(bookDocument);
        when(bookDocument.update(anyMap())).thenReturn(mock(ApiFuture.class));

        // Act
        firestoreService.updateBook(bookId, updatedBook);

        // Assert
        verify(bookDocument).update(anyMap());  // Verify if Firestore update method was called
    }

    @Test
    public void testDeleteBook() throws ExecutionException, InterruptedException {
        // Arrange
        String bookId = "123";
        when(db.collection("book")).thenReturn(booksCollection);
        when(booksCollection.document(bookId)).thenReturn(bookDocument);
        when(bookDocument.delete()).thenReturn(mock(ApiFuture.class));

        // Act
        firestoreService.deleteBook(bookId);

        // Assert
        verify(bookDocument).delete();  // Verify if Firestore delete method was called
    }

    @Test
    public void testAuthenticateUser() throws ExecutionException, InterruptedException {
        // Arrange
        String username = "user1";
        String password = "password123";
        User expectedUser = new User("user1", "password123", "user1@example.com", "John Doe", "1234567890", "user");
        when(db.collection("user")).thenReturn(mock(CollectionReference.class));
        when(db.collection("user").whereEqualTo("username", username).whereEqualTo("password", password).get()).thenReturn(mock(ApiFuture.class));

        // Act
        User actualUser = firestoreService.authenticateUser(username, password);

        // Assert
        assertNotNull(actualUser);
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
    }
}
