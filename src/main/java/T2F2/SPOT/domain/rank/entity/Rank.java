package T2F2.SPOT.domain.rank.entity;

import T2F2.SPOT.domain.user.entity.User;
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

    private String rankName;

    private String rankImageUrl;

    @OneToMany(mappedBy = "rank")
    private List<User> users = new ArrayList<>();
}
