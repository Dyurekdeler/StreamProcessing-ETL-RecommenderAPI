package com.dyurekdeler.RecommenderRESTAPI;

import com.dyurekdeler.RecommenderRESTAPI.models.Bestseller;
import com.dyurekdeler.RecommenderRESTAPI.service.BestsellerService;
import com.dyurekdeler.RecommenderRESTAPI.service.ProductViewHistoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductViewHistoryService mockProductViewHistoryService;

    @MockBean BestsellerService mockBestsellerService;

    @Test
    void shouldGetLastViewedProducts() throws Exception {
        String mockUserId = "user-73";
        List<String> mockProductIds = Arrays.asList("product-146","product-321","product-28","product-78","product-122","product-133","product-186","product-390","product-316","product-342");

        Mockito.when(mockProductViewHistoryService.getLastViewedProducts(mockUserId)).thenReturn(mockProductIds);

        mvc.perform(get("/api/products-viewed?user-id="+mockUserId))
                .andExpect(content().string("{\"userId\":\"user-73\",\"productIds\":[\"product-146\",\"product-321\",\"product-28\",\"product-78\",\"product-122\",\"product-133\",\"product-186\",\"product-390\",\"product-316\",\"product-342\"],\"type\":\"personalized\"}"));


    }

    @Test
    void shouldDeleteProductViewHistory() throws Exception {
        String mockUserId = "user-deniz";
        String mockProductId = "product-deniz";
        Boolean mockResult = true;

        Mockito.when(mockProductViewHistoryService.deleteProductViewHistory(mockUserId, mockProductId)).thenReturn(mockResult);

        mvc.perform(delete("/api/delete-product-view-history?user-id="+mockUserId+"&product-id="+mockProductId))
                .andExpect(content().string("true"));


    }

    @Test
    void shouldGetProductRecommendation() throws Exception {
        String mockUserId = "user-73";
        List<String> mockProductIds = Arrays.asList("product-172","product-57","product-149","product-123","product-102","product-549","product-391","product-291","product-582","product-563","product-170","product-83","product-20","product-136","product-173","product-135","product-100","product-27","product-15","product-185","product-116","product-36","product-34","product-75","product-120","product-56","product-25","product-130","product-150","product-54");

        ArrayList<String> mockProductIdsArrayList = new ArrayList<String>(mockProductIds);

        Bestseller mockBestseller = new Bestseller(mockUserId, mockProductIdsArrayList, "personalized");

        Mockito.when(mockBestsellerService.getProductRecommendation(mockUserId)).thenReturn(mockBestseller);

        mvc.perform(get("/api/get-product-recommendation?user-id="+mockUserId))
                .andExpect(content().string("{\"userId\":\"user-73\",\"productIds\":[\"product-172\",\"product-57\",\"product-149\",\"product-123\",\"product-102\",\"product-549\",\"product-391\",\"product-291\",\"product-582\",\"product-563\",\"product-170\",\"product-83\",\"product-20\",\"product-136\",\"product-173\",\"product-135\",\"product-100\",\"product-27\",\"product-15\",\"product-185\",\"product-116\",\"product-36\",\"product-34\",\"product-75\",\"product-120\",\"product-56\",\"product-25\",\"product-130\",\"product-150\",\"product-54\"],\"type\":\"personalized\"}"));


    }


}
