package aaagt.cloudservice.app.controller;

import aaagt.cloudservice.App;
import aaagt.cloudservice.app.HomeController;
import aaagt.cloudservice.jwt.config.JwtConfig;
import aaagt.cloudservice.jwt.config.JwtProperties;
import aaagt.cloudservice.security.config.SecurityConfig;
import aaagt.cloudservice.security.config.authentication.JwtAuthenticationEntryPoint;
import aaagt.cloudservice.security.config.authentication.JwtAuthenticationFilter;
import aaagt.cloudservice.security.web.LoginService;
import aaagt.cloudservice.user.repository.UserRepository;
import aaagt.cloudservice.user.repository.UserTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
        SecurityConfig.class,
        JwtConfig.class,
        JwtAuthenticationEntryPoint.class,
        JwtAuthenticationFilter.class
})
@WebMvcTest(controllers = {HomeController.class})
@ContextConfiguration(classes = {App.class})
@EnableConfigurationProperties(value = JwtProperties.class)
class HomeControllerTest {

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
    void home_WhenAnonymous_ThenStatusIsOk() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello anonimous user"));
    }

    @Test
    @WithMockUser(username = "mockUser")
    void home_WhenUser_ThenHelloUsername() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello mockUser"));
    }

}
