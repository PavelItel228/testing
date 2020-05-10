package kpi.prject.testing.testing;

import kpi.prject.testing.testing.entity.enums.ReportStatus;
import kpi.prject.testing.testing.repository.ReportsRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create_user_before.sql", "/create_report_before.sql", "/create_archive_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class InspectorHomeTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReportsRepository reportsRepository;

    @Test
    @WithUserDetails("user@gmail.com")
    public void getHomeUser() throws Exception {
        this.mockMvc.perform(get("/inspHome"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    public void getHomeAdmin() throws Exception {
        this.mockMvc.perform(get("/inspHome"))
                .andExpect(status().isOk());
    }


    @Test
    @WithUserDetails("admin@gmail.com")
    public void declinePost () throws Exception {
        this.mockMvc.perform(post("/inspHome/decline/1")
                .with(csrf().asHeader())
                .content(buildUrlEncodedFormEntity(
                        "reason", "reason"
                ))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
        Assert.assertEquals(ReportStatus.NOT_ACCEPTED, reportsRepository.findById(1L).get().getStatus());
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    public void acceptPost () throws Exception {
        this.mockMvc.perform(post("/inspHome/accept/1")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
        Assert.assertEquals(ReportStatus.ACCEPTED, reportsRepository.findById(1L).get().getStatus());
    }

    private String buildUrlEncodedFormEntity(String... params) {
        if( (params.length % 2) > 0 ) {
            throw new IllegalArgumentException("Need to give an even number of parameters");
        }
        StringBuilder result = new StringBuilder();
        for (int i=0; i<params.length; i+=2) {
            if( i > 0 ) {
                result.append('&');
            }
            try {
                result.
                        append(URLEncoder.encode(params[i], StandardCharsets.UTF_8.name())).
                        append('=').
                        append(URLEncoder.encode(params[i+1], StandardCharsets.UTF_8.name()));
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return result.toString();
    }
}
