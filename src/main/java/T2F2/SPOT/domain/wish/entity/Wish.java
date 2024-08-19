package T2F2.SPOT.domain.wish.entity;

import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Wish extends BaseEntity {

    @Id
    @Column(name = "wish_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
