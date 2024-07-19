package T2F2.SPOT.domain.wish.entity;

import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.rank.entity.Rank;
import T2F2.SPOT.domain.user.entity.User;
import jakarta.persistence.*;

@Entity
public class Wish {

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
