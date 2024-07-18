package T2F2.SPOT.domain.rank.entity;

import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.wish.entity.Wish;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Rank {

    @Id
    @GeneratedValue
    @Column(name = "rank_id")
    private Long id;

    private String rank_name;

    private String rank_image_url;
    @OneToMany(mappedBy = "user")
    private List<User> users = new ArrayList<>();
}
