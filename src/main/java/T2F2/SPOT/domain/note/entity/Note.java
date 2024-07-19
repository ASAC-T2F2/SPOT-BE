package T2F2.SPOT.domain.note.entity;

import T2F2.SPOT.domain.user.entity.User;
import jakarta.persistence.*;

@Entity
public class Note {

    @Id
    @GeneratedValue
    @Column(name = "note_id")
    private Long id;

    private String note_content;

    // 작성일
    // 삭제일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_room_id")
    private NoteRoom noteRoom;
}
