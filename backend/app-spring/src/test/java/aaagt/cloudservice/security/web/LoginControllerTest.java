package aaagt.cloudservice.security.web;

import aaagt.cloudservice.App;
import aaagt.cloudservice.app.controller.HomeController;
import aaagt.cloudservice.jwt.config.JwtConfig;
import aaagt.cloudservice.jwt.config.JwtProperties;
import aaagt.cloudservice.jwt.service.impl.JwtServiceImpl;
import aaagt.cloudservice.security.config.SecurityConfig;
import aaagt.cloudservice.security.config.authentication.JwtAuthenticationEntryPoint;
import aaagt.cloudservice.security.config.authentication.JwtAuthenticationFilter;
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

/*@WebMvcTest({HomeController.class, LoginController.class})
@ContextConfiguration(classes = {App.class})
//@AutoConfigureMockMvc
//@ContextConfiguration(classes = {SecurityConfig.class})
@Import({SecurityConfig.class})
//@SpringBootConfiguration
//@AutoConfigureDataJpa
@ExtendWith(MockitoExtension.class)*/

@Import({
        SecurityConfig.class,
        JwtServiceImpl.class,
        JwtAuthenticationEntryPoint.class,
        JwtAuthenticationFilter.class})
@WebMvcTest(controllers = {LoginController.class, HomeController.class})
@ContextConfiguration(classes = {
        App.class,
        SecurityConfig.class,
        JwtConfig.class
})
@EnableConfigurationProperties(value = JwtProperties.class)
//@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    MockMvc mvc;

    /*@Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;*/
    /*@Autowired
    private AuthenticationManager authenticationManager;*/
    /*@PersistenceContext
    EntityManager entityManager;*/
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
    /*


     */


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

   /* @Test
    void 토큰을_생성한다() {
        //given
        given(loginService.createToken(any()))
                .willReturn(TokenResponse.of(accessToken));

        //when
        TokenRequest params = new TokenRequest(GithubResponses.소롱.getCode());
        ValidatableMockMvcResponse response = given
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all();

        //then
        response.expect(status().isOk());

        //docs
        response.apply(document("login/token"));
    }*/

}
