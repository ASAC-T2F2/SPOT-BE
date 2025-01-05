package T2F2.SPOT.domain.post.entity;

import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.post.Category;
import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.dto.CreatePostDto;
import T2F2.SPOT.domain.post.dto.ModifyPostDto;
import T2F2.SPOT.domain.review.entity.Review;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.wish.entity.Wish;
import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    private int price;

    @ColumnDefault("0")
    private int viewCount;

    @Enumerated(EnumType.STRING)
    private PostFor postFor;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @ColumnDefault("FALSE")
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Wish> wishes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<NoteRoom> noteRooms = new ArrayList<>();

    private Post(String title, String content, PostFor postFor, int price, Category category, User user) {
        this.title = title;
        this.content = content;
        this.postFor = postFor;
        this.price = price;
        this.category = category;
        this.user = user;
        this.isDeleted = false;
        this.postStatus = PostStatus.TRADING;
    }


    public static Post of(CreatePostDto createPostDto, User user) {
        return new Post(
                createPostDto.getTitle(),
                createPostDto.getContent(),
                createPostDto.getPostFor(),
                createPostDto.getPrice(),
                createPostDto.getCategory(),
                user
        );
    }

    public void updatePostStatus(String postStatus) {
        switch (postStatus) {
            case "DELETE" -> this.isDeleted = true;
            case "TRADING" -> this.postStatus = PostStatus.TRADING;
            case "TRADE_COMPLETE" -> this.postStatus = PostStatus.TRADE_COMPLETE;
        }
    }

    public void modifyPost(ModifyPostDto modifyPostDto) {
        this.title = modifyPostDto.getTitle();
        this.content = modifyPostDto.getContent();
        this.price = modifyPostDto.getPrice();

    }


    public String getFirstImageOrDefault() {
        return postImages.isEmpty() ? "default" : postImages.get(0).toString();
    }
}