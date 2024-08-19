package T2F2.SPOT.domain.wish.entity;

import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Wish(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
