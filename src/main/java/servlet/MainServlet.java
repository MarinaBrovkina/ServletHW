package servlet;

import controller.PostController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.PostRepository;
import service.PostService;
import service.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;

    @Override
    public void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext("java");
        controller = context.getBean(PostController.class);
    }

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
        Service service = new Service(controller);
        service.service(req, resp);
    }
}


