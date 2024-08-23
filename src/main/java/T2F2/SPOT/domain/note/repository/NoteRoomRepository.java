package T2F2.SPOT.domain.note.repository;

import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRoomRepository extends JpaRepository<NoteRoom, Long> {

    NoteRoom findByPostAndSenderAndReceiver(Post post, User sender, User receiver);
}
