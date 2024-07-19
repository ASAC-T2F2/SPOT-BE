package T2F2.SPOT.domain.post.entity;

import T2F2.SPOT.domain.category.entity.Category;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.review.entity.Review;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.wish.entity.Wish;
import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Post extends BaseEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String post_title;

    private String post_content;

    private PostFor postFor;

    private PostStatus postStatus;

    private String price;

    private Boolean is_deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post")
    private List<PostImage> postImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "post")
    private Review review;

    @OneToMany(mappedBy = "post")
    private List<Wish> wishes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<NoteRoom> noteRooms = new ArrayList<>();
}
