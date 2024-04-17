package repository;

import exception.NotFoundException;
import model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final AtomicLong idCounter;
    private final List<Post> postList;

    public PostRepository() {
        this.postList = new ArrayList<>();
        this.idCounter = new AtomicLong(1);
    }

    public List<Post> all() {
        return Collections.unmodifiableList(postList);
    }

    public Optional<Post> getById(long id) {
        return postList.stream()
                .filter(post -> post.getId() == id)
                .findFirst();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long newId = idCounter.getAndIncrement();
            post.setId(newId);
            postList.add(post);
            return post;
//            for (int i = 0; i < postList.size(); i++) {
//                if (postList.get(i).getId() == post.getId()) {
//                    postList.set(i, post);
//                    return post;
//                }
//            }

        } else {
            Optional<Post> optionalPost = postList.stream()
                    .filter(p -> p.getId() == post.getId())
                    .findFirst();

            if (optionalPost.isPresent()) {
                int index = postList.indexOf(optionalPost.get());
                postList.set(index, post);
                return post;
            } else {
                throw new NotFoundException("Post with id " + post.getId() + " not found");
            }
        }
    }

    public void removeById(long id) {
        postList.removeIf(post -> post.getId() == id);
    }
}
