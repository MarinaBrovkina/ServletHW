package servlet;

import controller.PostController;
import repository.PostRepository;
import service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {

        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            switch (method) {
                case PostController.GET_METHOD:
                    if (path.equals(PostController.POSTS_PATH)) {
                        controller.all(resp);
                    } else if (path.matches(PostController.POSTS_PATH + "/\\d+")) {
                        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
                        controller.getById(id, resp);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                    break;
                case PostController.POST_METHOD:
                    if (path.equals(PostController.POSTS_PATH)) {
                        controller.save(req.getReader(), resp);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                    break;

                case PostController.DELETE_METHOD:
                    if (path.matches(PostController.POSTS_PATH + "/\\d+")) {
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

