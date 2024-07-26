package T2F2.SPOT.domain.grade.entity;

import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Grade extends BaseEntity {

    @Id
    @Column(name = "grade_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rankName;

    private String rankImageUrl;

    @OneToMany(mappedBy = "grade")
    private List<User> users = new ArrayList<>();
}
