package ru.otus.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import ru.otus.spring.domain.*;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(User.class);
        config.exposeIdsFor(Book.class);
        config.exposeIdsFor(Comment.class);
        config.exposeIdsFor(Author.class);
        config.exposeIdsFor(Genre.class);
        config.setDefaultMediaType(MediaType.APPLICATION_JSON);
        config.useHalAsDefaultJsonMediaType(false);
        config.setDefaultPageSize(100);
    }
}
