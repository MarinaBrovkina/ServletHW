package servlet;

import controller.PostController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class MainServlet extends HttpServlet {
    private PostController controller;

    @Override
    public void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext("java");
        controller = context.getBean(PostController.class);
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        Service service = new Service(controller);
        service.service(req, resp);
    }
}

