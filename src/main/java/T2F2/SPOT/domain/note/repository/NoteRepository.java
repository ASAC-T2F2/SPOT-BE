package T2F2.SPOT.domain.note.repository;

import T2F2.SPOT.domain.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByNoteRoomId(Long noteRoomId);
}
