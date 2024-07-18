package T2F2.SPOT.domain.post.entity;

import jakarta.persistence.*;

@Entity
public class Post_Image {
    @Id
    @GeneratedValue
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    private Post post;

    private String image_url;
}
