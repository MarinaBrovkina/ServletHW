package repository;

import model.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final AtomicLong idCounter;
    private final ConcurrentHashMap<Long, Post> postMap;

    public PostRepository() {
        this.postMap = new ConcurrentHashMap<>();
        this.idCounter = new AtomicLong(1);
    }

    public List<Post> all() {
        return Collections.unmodifiableList(new ArrayList<>(postMap.values()));
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(postMap.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long newId = idCounter.getAndIncrement();
            post.setId(newId);
        }

        postMap.put(post.getId(), post);
        return post;
    }

    public void removeById(long id) {
        postMap.remove(id);
    }
}