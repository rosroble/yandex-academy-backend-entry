package ru.rosroble;

import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.rosroble.Batches.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
   public class RestApiTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    private void importsExpect400(String content) throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(1)
    public void test1_imports_noUpdateDate_expect400() throws Exception {
        importsExpect400(noUpdateDateImportRequest);
    }

    @Test
    @Order(2)
    public void test2_imports_unexpectedField_expect400() throws Exception {
        importsExpect400(unexpectedFieldImportRequest);
    }

    @Test
    @Order(3)
    public void test3_imports_badPrice_expect400() throws Exception {
        importsExpect400(negativePriceImportRequest);
        importsExpect400(badPriceTypeImportRequest);
        importsExpect400(noPriceOfferImportRequest);
        importsExpect400(notNullPriceCategoryImportRequest);
    }

    @Test
    @Order(4)
    public void test4_imports_badUUID_expect400() throws Exception {
        importsExpect400(badUUIDimport1);
        importsExpect400(badUUIDimport2);
        importsExpect400(badUUIDimport3);
    }

    @Test
    @Order(5)
    public void test5_imports_parentNotFound_expect400() throws Exception {
        importsExpect400(importNoParentFoundBatch);
    }

    @Test
    @Order(6)
    public void test6_imports_selfParentImport_expect400() throws Exception {
        importsExpect400(importSelfParentBatch);
    }

    @Test
    @Order(7)
    public void test7_imports_offerAsParent_expect400() throws Exception {
        importsExpect400(offerAsParent);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Order(8)
    public void test8_imports_tree_expect200() throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                        .content(categoryPriceCheck))
                .andExpect(status().isOk());
        mockMvc.perform(get("/nodes/6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2"))
                .andExpect(status().isOk())
                .andExpect(content().json(categoryPriceCheckNodes1C));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Order(9)
    public void test9_delete_checkEntryIsRemoved_expect404() throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                        .content(categoryPriceCheck))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/delete/6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/delete/6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Order(10)
    public void test10_imports_treeByParts_expect200() throws Exception {
        for (int i = 0; i < categoryPriceCheckByParts.length; i++) {
            mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                    .content(categoryPriceCheckByParts[i]))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/nodes/6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(categoryPriceCheckByPartsNodes1C[i]));
        }
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Order(11)
    public void test11_sales_testBatch_expect200() throws Exception {
        test10_imports_treeByParts_expect200();
        mockMvc.perform(get("/sales")
                        .param("date", "2022-06-06T00:00:01.000Z"))
                .andExpect(status().isOk())
                .andExpect(content().json(salesExpectedBatch));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Order(12)
    public void test12_delete_checkCategoryPricesAreUpdatedAfterDelete_expect200() throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                .content(categoryPriceCheck))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/delete/38da9384-fc68-4ff4-ad9e-4620227bd232"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/nodes/6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2"))
                .andExpect(status().isOk())
                .andExpect(content().json(nodesAfter8CDelete));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Order(13)
    public void test13_nodes_checkCategoryPricesAreUpdatedAfterParentChange_expect200() throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                .content(categoryPriceCheck))
                .andExpect(status().isOk());
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                .content(categoryPriceCheckChangeParent))
                .andExpect(status().isOk());
        mockMvc.perform(get("/nodes/6aed457f-8a26-44c0-ac5e-a9d8d4b3c6c2"))
                .andExpect(status().isOk())
                .andExpect(content().json(categoryPriceCheckChangeParentNodes1C));
        mockMvc.perform(get("/nodes/07698dfe-cba2-4a9e-8f6d-50fa3bc12048"))
                .andExpect(status().isOk())
                .andExpect(content().json(categoryPriceCheckChangeParentNodes4C));
    }

    @Test
    @Order(14)
    public void test14_nodes_itemNotFoundAfterDelete_expect404() throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                .content(categoryPriceCheck))
                .andExpect(status().isOk());
        mockMvc.perform(get("/nodes/07698dfe-cba2-4a9e-8f6d-50fa3bc12048"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/delete/07698dfe-cba2-4a9e-8f6d-50fa3bc12048"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/nodes/07698dfe-cba2-4a9e-8f6d-50fa3bc12048"))
                .andExpect(status().isNotFound());

    }

    @Test
    @Order(15)
    public void test15_sales_invalidDateParameter_expect400() throws Exception {
        mockMvc.perform(get("/sales")
                .param("date", "2022-05-28TM21:12:01.516Z"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(16)
    public void test16_sales_dateIsOutOfRange_expect400() throws Exception {
        mockMvc.perform(get("/sales")
                .param("date", "10000000-01-01T00:00:00.000Z"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/sales")
                .param("date", "1969-01-01T00:00:00.000Z"))
                .andExpect(status().isBadRequest());
    }

}
