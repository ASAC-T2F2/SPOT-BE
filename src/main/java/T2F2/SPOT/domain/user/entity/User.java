package T2F2.SPOT.domain.user.entity;

import T2F2.SPOT.domain.grade.entity.Grade;
import T2F2.SPOT.domain.note.entity.Note;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.review.entity.Review;
import T2F2.SPOT.domain.wish.entity.Wish;
import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String nickname;
    private String university;
    private String major;
    private String entranceYear;
    private Boolean isDeleted;
    private String imageUrl;

    // 생성 일자
    // 탈퇴 일자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wish> wishes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<NoteRoom> noteRooms = new ArrayList<>();
}

