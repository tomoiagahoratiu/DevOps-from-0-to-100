package com.fortech.igrow;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class IgrowApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllBooksExpect200IsOk() throws Exception {
        String expectedResponse = "{\"data\":{\"books\":[" +
                "{\"id\":1,\"name\":\"Mogli\"}," +
                "{\"id\":2,\"name\":\"Cenusareasa\"}," +
                "{\"id\":3,\"name\":\"Heidi\"}," +
                "{\"id\":4,\"name\":\"Winnetou\"}" +
                "]}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/graphql")
                .content("{\"query\":\"{ books { id name } }\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse))
                .andReturn();
    }

    @Test
    public void getBookByIdExpect200isOk() throws Exception {
        String expectedResponse = "{\"data\":{\"book\":" +
                "{\"name\":\"Mogli\", \"isAvailable\":true}}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/graphql")
                .content("{\"query\":\"{ book(id:1) { name, isAvailable } }\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse))
                .andReturn();
    }
}
