package T2F2.SPOT.domain.note.service;

import T2F2.SPOT.domain.note.dto.NoteRequest;
import T2F2.SPOT.domain.note.dto.NoteResponse;
import T2F2.SPOT.domain.note.entity.Note;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.note.repository.NoteRepository;
import T2F2.SPOT.domain.note.repository.NoteRoomRepository;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NoteServiceImpl {

    private final NoteRoomRepository noteRoomRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteResponse sendNote(Long roomId, String senderNickname, NoteRequest noteRequest) {

        NoteRoom room = noteRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("NoteRoom not found"));

        User sender = userRepository.findByNickname(senderNickname)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Note newNote = Note.createNote(noteRequest.getNoteContent(), sender, room);
        Note savedNote = noteRepository.save(newNote);
        log.info("Saved new note with ID: {}", savedNote.getId());

        return new NoteResponse(
                savedNote.getSender().getNickname(),
                savedNote.getNoteContent()
        );
    }

    @Transactional(readOnly = true)
    public List<Note> findAllNoteByNoteRoomId(Long noteRoomId) {
        return noteRepository.findAllByNoteRoomId(noteRoomId);
    }
}
