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
                .andExpect(status().isBadRequest());
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
    public void imports_parentNotFound_expect400() throws Exception {
    }


}
