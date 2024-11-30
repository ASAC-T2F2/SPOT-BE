package T2F2.SPOT.domain.note.entity;

import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoteRoom extends BaseEntity {

    @Id
    @Column(name = "note_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String roomName;

    @OneToMany(mappedBy = "noteRoom", cascade = CascadeType.ALL)
    private List<Note> notes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "owner_id")
//    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private User guest;

    @Builder
    public NoteRoom(Post post, User guest) {
        this.post = post;
        this.guest = guest;
    }

    public static NoteRoom createRoom(Post post, User guest) {
        return NoteRoom.builder()
                .post(post)
                .guest(guest)
                .build();
    }
}
