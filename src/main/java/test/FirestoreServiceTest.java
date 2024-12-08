package test;

import com.example.warehouse.model.Book;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class FirestoreServiceTest {

    @Test

    @Mock
    private Firestore mockFirestore;
    @Mock
    private CollectionReference mockCollection;
    @Mock
    private DocumentReference mockDocument;
    @Mock
    private ApiFuture<DocumentSnapshot> mockDocumentSnapshotApiFuture;
    @Mock
    private ApiFuture<WriteResult> mockWriteResultApiFuture;
    @Mock
    private QuerySnapshot mockQuerySnapshot;
    @Mock
    private QueryDocumentSnapshot mockQueryDocumentSnapshot;

    private FirestoreService firestoreService;

    @BeforeEach
    public void setUp() {
        // Initialize mocks and FirestoreService
        MockitoAnnotations.openMocks(this);
        firestoreService = new FirestoreService();
        
        // Mock Firestore and other Firestore-related objects
        when(mockFirestore.collection("book")).thenReturn(mockCollection);
        when(mockFirestore.collection("user")).thenReturn(mockCollection);
    }

    @Test
    public void testCreateBook() throws InterruptedException, ExecutionException {
        // Arrange
        Book book = new Book("Title", "Author", "2021", "1234567890", 20.0, 10, "Description", "url", "Category");
        when(mockCollection.add(anyMap())).thenReturn(mockDocument);

        // Act
        firestoreService.createBook(book);

        // Assert
        verify(mockCollection).add(anyMap());
    }

    @Test
    public void testGetBookById() throws InterruptedException, ExecutionException {
        // Arrange
        String bookId = "book123";
        Map<String, Object> bookData = new HashMap<>();
        bookData.put("title", "Title");
        bookData.put("author", "Author");
        bookData.put("publishedYear", "2021");
        when(mockDocument.get()).thenReturn(mockDocumentSnapshotApiFuture);
        when(mockDocumentSnapshotApiFuture.get()).thenReturn(mockQueryDocumentSnapshot);
        when(mockQueryDocumentSnapshot.getData()).thenReturn(bookData);

        // Act
        Book result = firestoreService.getBookById(bookId);

        // Assert
        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    @Test
    public void testGetAllBooks() throws InterruptedException, ExecutionException {
        // Arrange
        Map<String, Object> bookData1 = new HashMap<>();
        bookData1.put("title", "Book 1");
        Map<String, Object> bookData2 = new HashMap<>();
        bookData2.put("title", "Book 2");
        List<QueryDocumentSnapshot> documentList = Arrays.asList(mockQueryDocumentSnapshot, mockQueryDocumentSnapshot);
        when(mockCollection.get()).thenReturn(mockDocumentSnapshotApiFuture);
        when(mockDocumentSnapshotApiFuture.get()).thenReturn(mockQuerySnapshot);
        when(mockQuerySnapshot.getDocuments()).thenReturn(documentList);

        // Act
        List<Book> books = firestoreService.getAllBooks();

        // Assert
        assertEquals(2, books.size());
    }

    @Test
    public void testUpdateBook() throws InterruptedException, ExecutionException {
        // Arrange
        String bookId = "book123";
        Book updatedBook = new Book("Updated Title", "Updated Author", "2022", "0987654321", 25.0, 5, "Updated Description", "updated_url", "Updated Category");
        when(mockDocument.update(anyMap())).thenReturn(mockWriteResultApiFuture);
        when(mockWriteResultApiFuture.get()).thenReturn(mock(WriteResult.class));

        // Act
        firestoreService.updateBook(bookId, updatedBook);

        // Assert
        verify(mockDocument).update(anyMap());
    }

    @Test
    public void testDeleteBook() throws InterruptedException, ExecutionException {
        // Arrange
        String bookId = "book123";
        when(mockDocument.delete()).thenReturn(mockWriteResultApiFuture);
        when(mockWriteResultApiFuture.get()).thenReturn(mock(WriteResult.class));

        // Act
        firestoreService.deleteBook(bookId);

        // Assert
        verify(mockDocument).delete();
    }

    @Test
    public void testAuthenticateUser() throws InterruptedException, ExecutionException {
        // Arrange
        String username = "user1";
        String password = "password";
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", "user1");
        userData.put("password", "password");
        when(mockCollection.whereEqualTo("username", username).whereEqualTo("password", password)).thenReturn(mock(Query.class));
        when(mockCollection.get()).thenReturn(mockDocumentSnapshotApiFuture);
        when(mockDocumentSnapshotApiFuture.get()).thenReturn(mockQuerySnapshot);
        when(mockQuerySnapshot.isEmpty()).thenReturn(false);
        when(mockQuerySnapshot.getDocuments()).thenReturn(Arrays.asList(mockQueryDocumentSnapshot));
        when(mockQueryDocumentSnapshot.toObject(User.class)).thenReturn(new User("user1", "password"));

        // Act
        User result = firestoreService.authenticateUser(username, password);

        // Assert
        assertNotNull(result);
        assertEquals("user1", result.getUsername());
    }
}
