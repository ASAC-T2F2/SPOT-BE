package T2F2.SPOT.domain.note.service;

import T2F2.SPOT.domain.note.entity.Note;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.note.repository.NoteRepository;
import T2F2.SPOT.domain.note.repository.NoteRoomRepository;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl {

    private final NoteRoomRepository noteRoomRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public List<NoteRoom> findAllRoom() {
        return noteRoomRepository.findAll();
    }

    public NoteRoom findRoomById(Long id) {
        return noteRoomRepository.findById(id).orElseThrow();
    }

    public NoteRoom createRoom(Post post, User sender, User buyer) {
        return noteRoomRepository.save(NoteRoom.createRoom(post, sender, buyer));
    }

    public Note createNote(Long noteRoomId, String noteContent, String sender) {

        User senderName = userRepository.findByNickname(sender);

        NoteRoom room = noteRoomRepository.findById(noteRoomId).orElseThrow();

        return noteRepository.save(Note.createNote(noteContent, senderName, room));
    }

    public List<Note> findAllNoteByNoteRoomId(Long noteRoomId) {
        return noteRepository.findAllByNoteRoomId(noteRoomId);
    }
}
