package prince.aktiveskochbuch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import prince.aktiveskochbuch.adapter.apis.RezeptController;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class AktivesKochbuchApplicationTests {

    @Autowired
    private RezeptController rezeptController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        assertThat(rezeptController).isNotNull();
    }

    @Test
    void shouldLoad() throws Exception {
        this.mockMvc.perform(get("/rezepte/someTitle")).andDo(print()).andExpect(content().json("{\"statusCode\":404}"));
        this.mockMvc.perform(get("/rezepte")).andDo(print()).andExpect(content().json("{\"statusCode\":200}"));
        this.mockMvc.perform(get("/rezepte/Spaghetti Bolognese")).andDo(print()).andExpect(content().json("{\"statusCode\":200}"));
        this.mockMvc.perform(delete("/rezepte/1")).andDo(print()).andExpect(content().json("{\"statusCode\":200}"));
        this.mockMvc.perform(post("/rezepte").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"titel\":\"Spaghetti Bolognese\",\"rezeptur\":\"Spaghetti, Tomatensoße, Hackfleisch\",\"tags\":[\"Nudeln\",\"Fleisch\"]}"))
                .andDo(print())
                .andExpect(content().json("{\"statusCode\":200}"))
                .andReturn();
        this.mockMvc.perform(delete("/rezepte/11")).andDo(print()).andExpect(content().json("{\"statusCode\":404}"));


    }



}
