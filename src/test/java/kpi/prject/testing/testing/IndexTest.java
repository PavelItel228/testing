package kpi.prject.testing.testing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create_user_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class IndexTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithUserDetails("user@gmail.com")
    public void getIndexPage() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("user")));
    }

    @Test
    @WithUserDetails("user@gmail.com")
    public void getIndexLang() throws Exception {
        this.mockMvc.perform(get("/?lang=ua"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Головна")));
    }

    @Test
    public void getIndexUnauthorized() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")));
    }
}
