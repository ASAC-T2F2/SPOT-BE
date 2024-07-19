package T2F2.SPOT.domain.rank.entity;

import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.wish.entity.Wish;
import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Rank extends BaseEntity {

    @Id
    @Column(name = "rank_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rank_name;

    private String rank_image_url;
    @OneToMany(mappedBy = "user")
    private List<User> users = new ArrayList<>();
}
