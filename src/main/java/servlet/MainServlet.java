package servlet;

import controller.PostController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.PostRepository;
import service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private final String POSTS_PATH = "/api/posts";
    private final String GET_METHOD = "GET";
    private final String POST_METHOD = "POST";
    private final String DELETE_METHOD = "DELETE";

    @Configuration
    public class Config {

        @Bean
        public PostController postController() {
            return new PostController(postService());
        }

        @Bean
        public PostService postService() {
            return new PostService(postRepository());
        }

        @Bean
        public PostRepository postRepository() {
            return new PostRepository();
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {

        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            switch (method) {
                case GET_METHOD:
                    if (path.equals(POSTS_PATH)) {
                        controller.all(resp);
                    } else if (path.matches(POSTS_PATH + "/\\d+")) {
                        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
                        controller.getById(id, resp);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                    break;
                case POST_METHOD:
                    if (path.equals(POSTS_PATH)) {
                        controller.save(req.getReader(), resp);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                    break;

                case DELETE_METHOD:
                    if (path.matches(POSTS_PATH + "/\\d+")) {
                        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
                        controller.removeById(id, resp);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                    break;
                default:
                    resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

