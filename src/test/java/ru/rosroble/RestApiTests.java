package ru.rosroble;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class RestApiTests {

    private final String noUpdateDateImportRequest = """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66a444",
                  "name": "Оффер",
                  "price": 234,
                  "type": "OFFER"
                }
              ]
            }""";

    private final String unexpectedFieldImportRequest = """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66a444",
                  "name": "Оффер",
                  "owner: "zxc"
                  "price": 234,
                  "type": "OFFER"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"
            }""";

    private final String negativePriceImportRequest = """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66a444",
                  "name": "Оффер",
                  "price": -1,
                  "type": "OFFER"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"

            }""";

    private final String badPriceTypeImportRequest = """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66a444",
                  "name": "Оффер",
                  "price": 123abc,
                  "type": "OFFER"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"
            }""";

    private final String noPriceOfferImportRequest = """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66a444",
                  "name": "Оффер",
                  "type": "OFFER"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"
            }""";

    private final String notNullPriceCategoryImportRequest = """
            {
              "items": [
                {
                  "id": "3fa85f64-5717-4562-b3fc-2c963f66a444",
                  "name": "Оффер",
                  "price": 400,
                  "type": "CATEGORY"
                }
              ],
              "updateDate": "2022-05-28T21:12:01.516Z"
            }""";

    private final String categoryPriceCheck = """
            {
                    "items": [
                        {
                            "type": "CATEGORY",
                            "name": "1c",
                            "id": "1c"
                        },
                        {
                            "type": "OFFER",
                            "name": "2f",
                            "id": "2f",
                            "parentId": "1c",
                            "price": 60
                        },
                        {
                            "type": "OFFER",
                            "name": "3f",
                            "id": "3f",
                            "parentId": "1c",
                            "price": 90
                        },
                        {
                            "type": "CATEGORY",
                            "name": "4c",
                            "id": "4c",
                            "parentId": "1c"
                        },
                        {
                            "type": "CATEGORY",
                            "name": "5c",
                            "id": "5c",
                            "parentId": "4c"
                        },
                        {
                            "type": "CATEGORY",
                            "name": "6c",
                            "id": "6c",
                            "parentId": "4c"
                        },
                        {
                            "type": "OFFER",
                            "name": "7f",
                            "id": "7f",
                            "parentId": "5c",
                            "price": 100
                        },
                        {
                            "type": "CATEGORY",
                            "name": "8c",
                            "id": "8c",
                            "parentId": "5c"
                        },
                        {
                            "type": "OFFER",
                            "name": "9f",
                            "id": "9f",
                            "parentId": "8c",
                            "price": 80
                        },
                        {
                            "type": "OFFER",
                            "name": "10f",
                            "id": "10f",
                            "parentId": "8c",
                            "price": 60
                        }
                    ],
                    "updateDate": "2022-05-29T14:58:00Z"
                }""";

    private final String categoryPriceCheckNodes1C = """
            {
                "id": "1c",
                "name": "1c",
                "date": "2022-05-29T14:58:00.000Z",
                "parentId": null,
                "type": "CATEGORY",
                "price": 78,
                "children": [
                    {
                        "id": "2f",
                        "name": "2f",
                        "date": "2022-05-29T14:58:00.000Z",
                        "parentId": "1c",
                        "type": "OFFER",
                        "price": 60,
                        "children": null
                    },
                    {
                        "id": "3f",
                        "name": "3f",
                        "date": "2022-05-29T14:58:00.000Z",
                        "parentId": "1c",
                        "type": "OFFER",
                        "price": 90,
                        "children": null
                    },
                    {
                        "id": "4c",
                        "name": "4c",
                        "date": "2022-05-29T14:58:00.000Z",
                        "parentId": "1c",
                        "type": "CATEGORY",
                        "price": 80,
                        "children": [
                            {
                                "id": "6c",
                                "name": "6c",
                                "date": "2022-05-29T14:58:00.000Z",
                                "parentId": "4c",
                                "type": "CATEGORY",
                                "price": null,
                                "children": null
                            },
                            {
                                "id": "5c",
                                "name": "5c",
                                "date": "2022-05-29T14:58:00.000Z",
                                "parentId": "4c",
                                "type": "CATEGORY",
                                "price": 80,
                                "children": [
                                    {
                                        "id": "7f",
                                        "name": "7f",
                                        "date": "2022-05-29T14:58:00.000Z",
                                        "parentId": "5c",
                                        "type": "OFFER",
                                        "price": 100,
                                        "children": null
                                    },
                                    {
                                        "id": "8c",
                                        "name": "8c",
                                        "date": "2022-05-29T14:58:00.000Z",
                                        "parentId": "5c",
                                        "type": "CATEGORY",
                                        "price": 70,
                                        "children": [
                                            {
                                                "id": "9f",
                                                "name": "9f",
                                                "date": "2022-05-29T14:58:00.000Z",
                                                "parentId": "8c",
                                                "type": "OFFER",
                                                "price": 80,
                                                "children": null
                                            },
                                            {
                                                "id": "10f",
                                                "name": "10f",
                                                "date": "2022-05-29T14:58:00.000Z",
                                                "parentId": "8c",
                                                "type": "OFFER",
                                                "price": 60,
                                                "children": null
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }""";

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
    public void imports_tree_expect200() throws Exception {
        mockMvc.perform(post("/imports").contentType(MediaType.APPLICATION_JSON)
                        .content(categoryPriceCheck))
                .andExpect(status().isOk());
        mockMvc.perform(get("/nodes/1c")).andExpect(status().isOk()).andExpect(content().string(categoryPriceCheckNodes1C.trim()));
    }

    @Test
    public void imports_parentNotFound_expect400() throws Exception {
    }


}
