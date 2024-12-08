package test;

import com.example.warehouse.controller.CartScreenController;
import com.example.warehouse.model.Book;
import com.example.warehouse.model.Cart;
import com.example.warehouse.screen.CartScreen;
import com.example.warehouse.service.FirestoreService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestOfWarehouse {

    private CartScreenController controller;
    private Cart cartMock;
    private CartScreen cartScreenMock;

    @BeforeEach
    void setUp() {
        controller = new CartScreenController();
        cartMock = mock(Cart.class);
        cartScreenMock = mock(CartScreen.class);
    }

    @Test
    void testLoadBooks_ReturnsBookListWithQuantities() {
        // Arrange
        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(new Book("Book1", "Author1", "2023", "ISBN1", 100.0, 2, "Description1", "url1", "Category1"));
        mockBooks.add(new Book("Book2", "Author2", "2022", "ISBN2", 200.0, 3, "Description2", "url2", "Category2"));

        Cart cartMock = mock(Cart.class);
        when(cartMock.getBooks()).thenReturn(mockBooks);

        // Act
        List<CartScreen.BookWithQuantity> result = controller.loadBooks(mockBooks);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Book1", result.get(0).getBook().getTitle());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals("Book2", result.get(1).getBook().getTitle());
        assertEquals(3, result.get(1).getQuantity());
    }

    @Test
    void testUpdateTotalAmount_CalculatesCorrectAmount() {
        // Arrange
        List<CartScreen.BookWithQuantity> mockBooks = new ArrayList<>();
        mockBooks.add(new CartScreen.BookWithQuantity(new Book("Book1", "Author1", "2023", "ISBN1", 100.0, 2, "Description1", "url1", "Category1")));
        mockBooks.get(0).setQuantity(2);

        mockBooks.add(new CartScreen.BookWithQuantity(new Book("Book2", "Author2", "2022", "ISBN2", 150.0, 3, "Description2", "url2", "Category2")));
        mockBooks.get(1).setQuantity(3);

        // Act
        double totalAmount = controller.updateTotalAmount(mockBooks);

        // Assert
        assertEquals(650.0, totalAmount, 0.01); // 2 * 100 + 3 * 150
    }

    @Test
    void testCancelOrder_ResetsQuantitiesAndUI() {
        // Arrange
        List<CartScreen.BookWithQuantity> mockBooks = new ArrayList<>();
        CartScreen.BookWithQuantity bookWithQuantity = mock(CartScreen.BookWithQuantity.class);
        mockBooks.add(bookWithQuantity);

        // Act
        controller.cancelOrder(mockBooks, cartScreenMock);

        // Assert
        verify(cartScreenMock).displayBooks(mockBooks);
        verify(cartScreenMock).updateTotalAmount(mockBooks);
        verify(bookWithQuantity).setQuantity(1);
    }
}
