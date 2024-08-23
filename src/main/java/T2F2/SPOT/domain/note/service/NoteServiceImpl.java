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

    public NoteRoom createRoom(Post post, User sender, User receiver) {
        NoteRoom existingRoom = noteRoomRepository.findByPostAndSenderAndReceiver(post, sender, receiver);
        if(existingRoom != null) {
            return existingRoom;
        }
        NoteRoom newRoom = NoteRoom.createRoom(post, sender, receiver);
        return noteRoomRepository.save(newRoom);
    }

    public Note createNote(Long noteRoomId, String noteContent, String senderNickname) {

        User sender = userRepository.findByNickname(senderNickname).orElseThrow(() -> new RuntimeException("User not found"));

        NoteRoom room = noteRoomRepository.findById(noteRoomId).orElseThrow(() -> new RuntimeException("NoteRoom not found"));

        return noteRepository.save(Note.createNote(noteContent, sender, room));
    }

    public List<Note> findAllNoteByNoteRoomId(Long noteRoomId) {
        return noteRepository.findAllByNoteRoomId(noteRoomId);
    }
}
