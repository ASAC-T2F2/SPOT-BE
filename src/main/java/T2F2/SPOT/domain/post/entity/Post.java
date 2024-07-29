package T2F2.SPOT.domain.post.entity;

import T2F2.SPOT.domain.category.entity.Category;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.dto.CreatePostDto;
import T2F2.SPOT.domain.review.entity.Review;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.wish.entity.Wish;
import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Post extends BaseEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private PostFor postFor;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    private String price;

    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Wish> wishes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<NoteRoom> noteRooms = new ArrayList<>();

    private Post(String title, String content, PostFor postFor, PostStatus postStatus, String price, User user) {
        this.title = title;
        this.content = content;
        this.postFor = postFor;
        this.postStatus = postStatus;
        this.price = price;
        this.user = user;
    }


    public static Post of(CreatePostDto createPostDto, User user) {
        return new Post(
                createPostDto.getTitle(),
                createPostDto.getContent(),
                createPostDto.getPostFor(),
                createPostDto.getPostStatus(),
                createPostDto.getPrice(),
                user
        );
    }

    public void updatePostStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }
}
