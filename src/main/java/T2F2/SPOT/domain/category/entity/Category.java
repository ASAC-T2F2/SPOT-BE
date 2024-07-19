package T2F2.SPOT.domain.category.entity;

import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.wish.entity.Wish;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category_name;

    @OneToMany(mappedBy = "post")
    private List<Post> posts = new ArrayList<>();
}
