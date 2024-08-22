package T2F2.SPOT.domain.note.repository;

import T2F2.SPOT.domain.note.entity.NoteRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRoomRepository extends JpaRepository<NoteRoom, Long> {
}
