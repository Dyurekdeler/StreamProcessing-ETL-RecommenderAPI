package com.dyurekdeler.RecommenderRESTAPI;

import com.dyurekdeler.RecommenderRESTAPI.models.ProductViewHistory;
import com.dyurekdeler.RecommenderRESTAPI.repository.ProductViewHistoryRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ApiControllerIntegrationTest {

    @Autowired
    private ProductViewHistoryRepository productViewHistoryRepository;

    @Test
    public void whenGetLastViewedProductsReturnProductIds() {
        Date date = new Date();
        String userId = "user-deniz";
        String productId = "product-deniz";

        List<String> productIdsMustBeReturned = Arrays.asList(productId);

        //save test data to in-memory db for testing
        ProductViewHistory productViewHistory = new ProductViewHistory();
        productViewHistory.setProductId(productId);
        productViewHistory.setClickTimestamp(new Timestamp(date.getTime()));
        productViewHistory.setEvent("ProductView");
        productViewHistory.setSource("mobile-web");
        productViewHistory.setMessageId("message-id-deniz");
        productViewHistory.setUserId(userId);
        productViewHistoryRepository.save(productViewHistory);

        // when
        List<String> foundProductIds = productViewHistoryRepository.getLastViewedProducts(userId);

        // then
        assertThat(foundProductIds).isEqualTo(productIdsMustBeReturned);
    }

    @Test
    public void whenDeleteProductViewHistoryAssertIsNull() {
        Date date = new Date();
        String userId = "user-deniz";
        String productId = "product-deniz";

        //save test data to in-memory db for testing
        ProductViewHistory productViewHistory = new ProductViewHistory();
        productViewHistory.setProductId(productId);
        productViewHistory.setClickTimestamp(new Timestamp(date.getTime()));
        productViewHistory.setEvent("ProductView");
        productViewHistory.setSource("mobile-web");
        productViewHistory.setMessageId("message-id-deniz");
        productViewHistory.setUserId(userId);
        productViewHistoryRepository.save(productViewHistory);

        // when
        productViewHistoryRepository.deleteProductViewHistory(userId, productId);

        // then
        assertThat(productViewHistoryRepository.getLastViewedProducts(userId)).isNullOrEmpty();
    }

}
