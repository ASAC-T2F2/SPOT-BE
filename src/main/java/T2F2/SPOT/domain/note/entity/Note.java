package T2F2.SPOT.domain.note.entity;

import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Note extends BaseEntity {

    @Id
    @Column(name = "note_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String noteContent;

    // 작성일
    // 삭제일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_room_id")
    private NoteRoom noteRoom;

    @Builder
    public Note(String noteContent, User sender, User receiver, NoteRoom noteRoom) {
        this.noteContent = noteContent;
        this.sender = sender;
        this.receiver = receiver;
        this.noteRoom = noteRoom;
    }

    public static Note createNote(String noteContent, User sender, User receiver, NoteRoom noteRoom) {
        return Note.builder()
                .noteContent(noteContent)
                .sender(sender)
                .receiver(receiver)
                .noteRoom(noteRoom)
                .build();
    }
}
