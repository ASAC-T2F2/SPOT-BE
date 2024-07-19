package T2F2.SPOT.domain.user.entity;

import T2F2.SPOT.domain.note.entity.Note;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.rank.entity.Rank;
import T2F2.SPOT.domain.review.entity.Review;
import T2F2.SPOT.domain.wish.entity.Wish;
import T2F2.SPOT.util.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String user_email;
    private String user_password;
    private String user_nickname;
    private String user_university;
    private String user_major;
    private String entrace_year;
    private Boolean is_deleted;
    private String image_url;

    // 생성 일자
    // 탈퇴 일자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank_id")
    private Rank rank;

    @OneToMany(mappedBy = "user")
    private List<Wish> wishes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<NoteRoom> noteRooms = new ArrayList<>();
}

