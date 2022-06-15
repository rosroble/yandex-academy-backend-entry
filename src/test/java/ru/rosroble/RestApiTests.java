package ru.rosroble;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.rosroble.Batches.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class RestApiTests {



    @Autowired
    private MockMvc mockMvc;

    private void importsExpect400(String content) throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andDo(print());
              //  .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void imports_noUpdateDate_expect400() throws Exception {
        importsExpect400(noUpdateDateImportRequest);
    }

    @Test
    public void imports_unexpectedField_expect400() throws Exception {
        importsExpect400(unexpectedFieldImportRequest);
    }

    @Test
    public void imports_badPrice_expect400() throws Exception {
        importsExpect400(negativePriceImportRequest);
        importsExpect400(badPriceTypeImportRequest);
        importsExpect400(noPriceOfferImportRequest);
        importsExpect400(notNullPriceCategoryImportRequest);
    }

    @Test
    public void imports_parentNotFound_expect400() throws Exception {
        importsExpect400(importNoParentFoundBatch);
    }

    @Test
    public void imports_selfParentImport_expect400() throws Exception {
        importsExpect400(importSelfParentBatch);
    }

    @Test
    public void imports_offerAsParent_expect400() throws Exception {
        importsExpect400(offerAsParent);
    }



    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void imports_tree_expect200() throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                        .content(categoryPriceCheck))
                .andExpect(status().isOk());
        mockMvc.perform(get("/nodes/1c"))
                .andExpect(status().isOk())
                .andExpect(content().json(categoryPriceCheckNodes1C));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void imports_treeByParts_expect200() throws Exception {
        for (int i = 0; i < categoryPriceCheckByParts.length; i++) {
            mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                    .content(categoryPriceCheckByParts[i]))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/nodes/1c"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(categoryPriceCheckByPartsNodes1C[i]));
        }
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void delete_checkEntryIsRemoved_expect404() throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                .content(categoryPriceCheck))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/delete/1c"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/delete/1c"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void delete_checkCategoryPricesAreUpdatedAfterDelete_expect200() throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                .content(categoryPriceCheck))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/delete/8c"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/nodes/1c"))
                .andExpect(status().isOk())
                .andExpect(content().json(nodesAfter8CDelete));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void nodes_checkCategoryPricesAreUpdatedAfterParentChange_expect200() throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                .content(categoryPriceCheck))
                .andExpect(status().isOk());
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                .content(categoryPriceCheckChangeParent))
                .andExpect(status().isOk());
        mockMvc.perform(get("/nodes/1c"))
                .andExpect(status().isOk())
                .andExpect(content().json(categoryPriceCheckChangeParentNodes1C));
        mockMvc.perform(get("/nodes/4c"))
                .andExpect(status().isOk())
                .andExpect(content().json(categoryPriceCheckChangeParentNodes4C));
    }

    @Test
    public void nodes_itemNotFoundAfterDelete_expect404() throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                .content(categoryPriceCheck))
                .andExpect(status().isOk());
        mockMvc.perform(get("/nodes/4c"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/delete/4c"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/nodes/4c"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void sales_invalidDateParameter_expect400() throws Exception {
        mockMvc.perform(get("/sales")
                .param("date", "2022-05-28TM21:12:01.516Z"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void sales_dateIsOutOfRange_expect400() throws Exception {
        mockMvc.perform(get("/sales")
                .param("date", "10000000-01-01T00:00:00.000Z"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void sales_testBatch_expect200() throws Exception {
        imports_treeByParts_expect200();
        mockMvc.perform(get("/sales")
                .param("date", "2022-06-06T00:00:01.000Z"))
                .andExpect(status().isOk())
                .andExpect(content().json(salesExpectedBatch));
    }



}
