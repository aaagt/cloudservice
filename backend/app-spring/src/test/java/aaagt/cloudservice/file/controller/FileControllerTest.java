package aaagt.cloudservice.file.controller;

import aaagt.cloudservice.App;
import aaagt.cloudservice.file.advice.FileAdvice;
import aaagt.cloudservice.file.service.FileService;
import aaagt.cloudservice.jwt.config.JwtConfig;
import aaagt.cloudservice.jwt.config.JwtProperties;
import aaagt.cloudservice.security.config.SecurityConfig;
import aaagt.cloudservice.security.config.authentication.JwtAuthenticationEntryPoint;
import aaagt.cloudservice.security.config.authentication.JwtAuthenticationFilter;
import aaagt.cloudservice.security.web.LoginService;
import aaagt.cloudservice.user.repository.UserRepository;
import aaagt.cloudservice.user.repository.UserTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

import static aaagt.cloudservice.fixture.MultipartResolver.getMultimap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
        SecurityConfig.class,
        JwtConfig.class,
        JwtAuthenticationEntryPoint.class,
        JwtAuthenticationFilter.class,
        FileAdvice.class})
@WebMvcTest(controllers = {FileController.class})
@ContextConfiguration(classes = {App.class})
@EnableConfigurationProperties(value = JwtProperties.class)
class FileControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    LoginService loginService;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    LogoutHandler logoutHandler;
    @MockBean
    FileService fileService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserTokenRepository userTokenRepository;

    @Test
    void putFile() {
    }

    @Nested
    @DisplayName("File Controller - deleteFile")
    class DeleteFileTests {


        @Test
        void deleteFile_WhenNoCredentials_ThenUnauthorized() throws Exception {
            mvc.perform(delete("/file"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.message").value("Bad credentials"))
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @WithMockUser(username = "mockUser")
        void deleteFile_WhenDeleteFile_ThenOK() throws Exception {
            // given
            String filename = "fff.json";

            // when
            var result = mvc.perform(
                    delete("/file")
                            .param("filename", filename)
            );

            // then
            result.andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "mockUser")
        void deleteFile_WhenFileNotFoundFile_ThenBadRequest() throws Exception {
            // given
            String filename = "fff.json";
            doThrow(new FileNotFoundException("File fff.json not found"))
                    .when(fileService)
                    .deleteFile(eq(filename));

            // when
            var result = mvc.perform(
                    delete("/file")
                            .param("filename", filename)
            );

            // then
            result.andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.message").value("File fff.json not found"))
                    .andExpect(jsonPath("$.id").value(3));
        }

    }

    @Nested
    @DisplayName("File Controller - getFile")
    class GetFileTests {

        @Test
        void getFile_WhenNoCredentials_ThenUnauthorized() throws Exception {
            mvc.perform(get("/file"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.message").value("Bad credentials"))
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @WithMockUser(username = "mockUser")
        void getFile_WhenGetFile_ThenOK() throws Exception {
            // given
            String filename = "fff.json";
            Resource resource = load("test_file.json");
            doReturn(resource)
                    .when(fileService)
                    .get(eq(filename));


            // when
            var result = mvc.perform(
                    get("/file")
                            .param("filename", filename)
            );

            // then
            var response = result.andReturn().getResponse();
            var multimap = getMultimap(response);
            assertEquals("hash123", multimap.get("hash").get(0).getContentAsString(StandardCharsets.UTF_8));
            assertEquals("{\"test\":\"test1\"}\n", multimap.get("file").get(0).getContentAsString(StandardCharsets.UTF_8));

            result.andExpect(status().isOk());
        }

        private Resource load(String filename) {
            var uri = getClass().getClassLoader().getResource(filename);
            return new UrlResource(uri);
        }

        @Test
        @WithMockUser(username = "mockUser")
        void getFile_WhenFileNotFoundFile_ThenBadRequest() throws Exception {
            // given
            String filename = "fff.json";
            doThrow(new FileNotFoundException("File fff.json not found"))
                    .when(fileService)
                    .get(eq(filename));

            // when
            var result = mvc.perform(
                    get("/file")
                            .param("filename", filename)
            );

            // then
            result.andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.message").value("File fff.json not found"))
                    .andExpect(jsonPath("$.id").value(3));
        }

    }

    @Nested
    @DisplayName("File Controller - postFile")
    class PostFileTests {

        @Test
        void postFile_WhenNoCredentials_ThenUnauthorized() throws Exception {
            mvc.perform(post("/file"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.message").value("Bad credentials"))
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @WithMockUser(username = "mockUser")
        void postFile_WhenPostFile_ThenOK() throws Exception {
            MockMultipartFile jsonFile = new MockMultipartFile("file", "",
                    "application/json", "{}".getBytes());
            String hash = "123";
            String filename = "fff.json";

            mvc.perform(multipart("/file")
                            .file("hash", hash.getBytes(StandardCharsets.UTF_8))
                            .file(jsonFile)
                            .param("filename", filename)
                    )
                    .andExpect(status().isOk());
        }

    }

    @Nested
    @DisplayName("File Controller - getList")
    class GetListTests {

        @Test
        void getList_WhenNoCredentials_ThenUnauthorized() throws Exception {
            mvc.perform(post("/list"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.message").value("Bad credentials"))
                    .andExpect(jsonPath("$.id").value(1));

        }

    }

    // mockMvc.perform(multipart("/doc").file("a1", "ABC".getBytes("UTF-8")));

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
