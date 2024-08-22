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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @Builder
    public NoteRoom(Post post, User sender, User buyer) {
        this.post = post;
        this.sender = sender;
        this.buyer = buyer;
    }

    public static NoteRoom createRoom(Post post, User sender, User buyer) {
        return NoteRoom.builder()
                .post(post)
                .sender(sender)
                .buyer(buyer)
                .build();
    }
}
