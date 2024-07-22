package T2F2.SPOT.domain.post.entity;

import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;

@Entity
public class PostImage extends BaseEntity {
    @Id
    @Column(name = "post_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String imageUrl;
}
