package aaagt.cloudservice.security.web;

import aaagt.cloudservice.App;
import aaagt.cloudservice.app.HomeController;
import aaagt.cloudservice.jwt.config.JwtConfig;
import aaagt.cloudservice.jwt.config.JwtProperties;
import aaagt.cloudservice.security.config.SecurityConfig;
import aaagt.cloudservice.security.config.authentication.JwtAuthenticationEntryPoint;
import aaagt.cloudservice.security.config.authentication.JwtAuthenticationFilter;
import aaagt.cloudservice.security.web.dto.LoginRequestDto;
import aaagt.cloudservice.user.repository.UserRepository;
import aaagt.cloudservice.user.repository.UserTokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
        JwtAuthenticationEntryPoint.class,
        JwtAuthenticationFilter.class})
@WebMvcTest(controllers = {
        LoginController.class,
        HomeController.class})
@ContextConfiguration(classes = {
        App.class,
        SecurityConfig.class,
        JwtConfig.class})
@EnableConfigurationProperties(value = JwtProperties.class)
class LoginControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    LoginService loginService;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    LogoutHandler logoutHandler;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserTokenRepository userTokenRepository;

    @Test
    void login_WhenNoCredentials_ThenBadRequest() throws Exception {
        this.mvc.perform(post("/login"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Bad credentials"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void login_WhenUser_ThenReturnToken() throws Exception {
        var body = """
                {
                	"login": "mockedUser",
                	"password": "password"
                }
                """;

        Mockito.when(
                this.loginService.login(new LoginRequestDto("mockedUser", "password"))
        ).thenReturn("mocked token");

        this.mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.auth-token").value("mocked token"));
    }


    @Test
    void login_WhenUserNotInBase_ThenBadRequest() throws Exception {
        var body = """
                {
                	"login": "mockedUser",
                	"password": "password"
                }
                """;

        Mockito.when(
                this.loginService.login(new LoginRequestDto("mockedUser", "password"))
        ).thenThrow(new UsernameNotFoundException("mockedUser"));

        this.mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Bad credentials"))
                .andExpect(jsonPath("$.id").value(1));
    }

}
