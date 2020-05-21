package kpi.prject.testing.testing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create_user_before.sql"},  executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AccountsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/accounts/login"));
    }

    @Test
    public void getLoginTest() throws Exception {
        this.mockMvc.perform(get("/accounts/login"))
                .andExpect(status().isOk());

    }

    @Test
    public void getRegistrationTest() throws Exception {
        this.mockMvc.perform(get("/accounts/registration"))
                .andExpect(status().isOk());
    }

    @Test
    public void correctUserLoginTest() throws Exception {
        this.mockMvc.perform(formLogin("/accounts/login").user("user@gmail.com").password("grib1111"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }


    @Test
    public void badCredentials() throws Exception {
        this.mockMvc.perform(formLogin("/accounts/login").user("admin@gmail.com").password("grib11111"))
                .andExpect(redirectedUrl("/accounts/login?error=true"));
    }

    @Test
    public void validRegistration () throws Exception {
        this.mockMvc.perform(post("/accounts/registration")
                .with(csrf().asHeader())
                .content(buildUrlEncodedFormEntity(
                        "username", "test",
                        "email", "test@test.com",
                        "password", "test1234",
                        "confirmPassword", "test1234"
                ))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void langTest() throws Exception {
        this.mockMvc.perform(get("/accounts/login?lang=ua"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Увійти")));
    }

    @Test
    @WithUserDetails("user@gmail.com")
    public void logoutGet() throws Exception {
        this.mockMvc.perform(get("/logout"))
                .andExpect(redirectedUrl("/accounts/login?logout"));
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
