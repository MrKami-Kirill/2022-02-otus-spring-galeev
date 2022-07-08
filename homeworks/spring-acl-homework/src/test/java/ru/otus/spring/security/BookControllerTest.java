package ru.otus.spring.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.spring.page.BookPageController;
import ru.otus.spring.rest.BookController;
import ru.otus.spring.service.impl.UserDetailsServiceImpl;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
@DisplayName("Тестирование REST запросов")
class BookControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private BookController bookController;


    private static final String API_BOOKS_URL = "/api/books/";
    private static final String API_BOOKS_ID_URL = "/api/books/1";
    private static final String API_BOOKS_ID_COMMENTS_URL = "/api/books/1/comments";
    private static final String API_BOOKS_ID_AUTHORS_URL = "/api/books/1/authors";
    private static final String API_BOOKS_ID_GENRES_URL = "/api/books/1/genres";

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @DisplayName("Тестирование запросов для пользователя с ролью ADMIN")
    @WithMockUser(
            username = "ADMIN",
            password = "passw0rd",
            roles = "ADMIN"
    )
    @Test
    void testAuthenticatedRequestsForAdminRole() throws Exception {
        mockMvc.perform(get(API_BOOKS_URL))
                .andExpect(status().isOk());
        mockMvc.perform(get(API_BOOKS_ID_URL))
                .andExpect(status().isOk());
        mockMvc.perform(get(API_BOOKS_ID_COMMENTS_URL))
                .andExpect(status().isOk());
        mockMvc.perform(get(API_BOOKS_ID_AUTHORS_URL))
                .andExpect(status().isOk());
        mockMvc.perform(get(API_BOOKS_ID_GENRES_URL))
                .andExpect(status().isOk());
        mockMvc.perform(post(API_BOOKS_URL)
                        .content("{\"name\": \"testBook\",\"commentText\": \"testCommentText\", \"authorName\": \"testAuthorName\", \"genreName\": \"testGenreName\"}").contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(put(API_BOOKS_ID_URL)
                        .content("{\"name\": \"testBookName\"}").contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(delete(API_BOOKS_ID_URL))
                .andExpect(status().isOk());
    }

    @DisplayName("Тестирование запросов для пользователя с ролью USER")
    @WithMockUser(
            username = "USER",
            password = "passw0rd",
            roles = "USER"
    )
    @Test
    void testAuthenticatedRequestsForUserRole() throws Exception {
        mockMvc.perform(get(API_BOOKS_URL))
                .andExpect(status().isOk());
        mockMvc.perform(get(API_BOOKS_ID_URL))
                .andExpect(status().isOk());
        mockMvc.perform(get(API_BOOKS_ID_COMMENTS_URL))
                .andExpect(status().isOk());
        mockMvc.perform(get(API_BOOKS_ID_AUTHORS_URL))
                .andExpect(status().isOk());
        mockMvc.perform(get(API_BOOKS_ID_GENRES_URL))
                .andExpect(status().isOk());
        mockMvc.perform(post(API_BOOKS_URL)
                        .content("{\"name\": \"testBook\",\"commentText\": \"testCommentText\", \"authorName\": \"testAuthorName\", \"genreName\": \"testGenreName\"}").contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
        mockMvc.perform(put(API_BOOKS_ID_URL)
                        .content("{\"name\": \"testBookName\"}").contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete(API_BOOKS_ID_URL))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Тестирование запросов для пользователя с ролью GUEST")
    @WithMockUser(
            username = "GUEST",
            password = "passw0rd",
            roles = "GUEST"
    )
    @Test
    void testAuthenticatedRequestsForGuestRole() throws Exception {
        mockMvc.perform(get(API_BOOKS_URL))
                .andExpect(status().isOk());
        mockMvc.perform(get(API_BOOKS_ID_URL))
                .andExpect(status().isForbidden());
        mockMvc.perform(get(API_BOOKS_ID_COMMENTS_URL))
                .andExpect(status().isForbidden());
        mockMvc.perform(get(API_BOOKS_ID_AUTHORS_URL))
                .andExpect(status().isForbidden());
        mockMvc.perform(get(API_BOOKS_ID_GENRES_URL))
                .andExpect(status().isForbidden());
        mockMvc.perform(post(API_BOOKS_URL)
                        .content("{\"name\": \"testBook\",\"commentText\": \"testCommentText\", \"authorName\": \"testAuthorName\", \"genreName\": \"testGenreName\"}").contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
        mockMvc.perform(put(API_BOOKS_ID_URL)
                        .content("{\"name\": \"testBookName\"}").contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete(API_BOOKS_ID_URL))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Тестирование запросов для пользователя без роли")
    @Test
    void testAuthenticatedRequestsWithoutRole() throws Exception {
        mockMvc.perform(get(API_BOOKS_URL))
                .andExpect(status().isFound());
        mockMvc.perform(get(API_BOOKS_ID_URL))
                .andExpect(status().isFound());
        mockMvc.perform(get(API_BOOKS_ID_COMMENTS_URL))
                .andExpect(status().isFound());
        mockMvc.perform(get(API_BOOKS_ID_AUTHORS_URL))
                .andExpect(status().isFound());
        mockMvc.perform(get(API_BOOKS_ID_GENRES_URL))
                .andExpect(status().isFound());
        mockMvc.perform(post(API_BOOKS_URL)
                        .content("{\"name\": \"testBook\",\"commentText\": \"testCommentText\", \"authorName\": \"testAuthorName\", \"genreName\": \"testGenreName\"}").contentType(APPLICATION_JSON))
                .andExpect(status().isFound());
        mockMvc.perform(put(API_BOOKS_ID_URL)
                        .content("{\"name\": \"testBookName\"}").contentType(APPLICATION_JSON))
                .andExpect(status().isFound());
        mockMvc.perform(delete(API_BOOKS_ID_URL))
                .andExpect(status().isFound());
    }
}
