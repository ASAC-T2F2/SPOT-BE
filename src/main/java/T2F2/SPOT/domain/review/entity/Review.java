package T2F2.SPOT.domain.review.entity;

import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.user.entity.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;


    private float rate;

    @Nullable
    private String message;

    // 생성 일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
