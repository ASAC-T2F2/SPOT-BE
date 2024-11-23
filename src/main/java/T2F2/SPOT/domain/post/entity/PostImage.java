package T2F2.SPOT.domain.post.entity;

import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.dto.CreatePostDto;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PostImage extends BaseEntity {
    @Id
    @Column(name = "post_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String imageUrl;

    private PostImage(Post post, String url) {

        this.post = post;
        this.imageUrl = url;
    }

    public PostImage() {

    }


    public static PostImage of(Post post, String url) {
        return new PostImage(
                post,
                url
        );
    }
}
