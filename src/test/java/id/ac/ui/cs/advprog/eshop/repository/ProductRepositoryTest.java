package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest
{
    @InjectMocks
    ProductRepository productRepository;
    @BeforeEach
    void setUp()
    {

    }
    @Test
    void testCreateAndFind()
    {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }
    @Test
    void testFindAllIfEmpty()
    {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct()
    {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        assertTrue(productIterator.hasNext());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testUpdateExistentProduct()
    {
        Product product = new Product();
        product.setProductId("cb678e9g-1c67-460e-8860-71af6af67bd9");
        product.setProductName("Sampo Cap Udin");
        product.setProductQuantity(67);
        productRepository.create(product);

        // Initialize another product with the same id.
        Product updatedProduct = new Product();
        updatedProduct.setProductId("cb678e9g-1c67-460e-8860-71af6af67bd9");
        updatedProduct.setProductName("Sampo Cap Galih");
        product.setProductQuantity(42);
        productRepository.update(updatedProduct);

        // Get the product in the repository with the same id. Make sure it is updated.
        Product result = productRepository.findById("cb678e9g-1c67-460e-8860-71af6af67bd9");
        assertNotNull(result);
        assertEquals(updatedProduct.getProductName(), result.getProductName());
        assertEquals(updatedProduct.getProductQuantity(), result.getProductQuantity());
    }

    @Test
    void testUpdateNonExistentProduct()
    {
        // Try to update a product that does not exist in the repository
        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId("cb678e9g-1c67-460e-8860-71af6af67bd9");
        nonExistentProduct.setProductName("Sampo Cap Udin");
        nonExistentProduct.setProductQuantity(67);

        Product result = productRepository.update(nonExistentProduct);

        assertNull(result);
    }
}
