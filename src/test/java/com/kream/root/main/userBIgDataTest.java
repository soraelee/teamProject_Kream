package com.kream.root.main;

import com.kream.root.Detail.service.ProductBigDataService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class userBIgDataTest {

    @Autowired
    ProductBigDataService productBigDataService;

    @Test
    public void testClickUp(){
        String userid = "aaa";
        Long prId = 10L;

        Assertions.assertNotNull(productBigDataService.ClickUp(userid, prId));
    }
}
