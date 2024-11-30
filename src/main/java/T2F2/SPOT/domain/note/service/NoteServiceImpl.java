package T2F2.SPOT.domain.note.service;

import T2F2.SPOT.domain.note.dto.NoteResponse;
import T2F2.SPOT.domain.note.entity.Note;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.note.repository.NoteRepository;
import T2F2.SPOT.domain.note.repository.NoteRoomRepository;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.repository.UserRepository;
import T2F2.SPOT.util.exception.CustomException;
import T2F2.SPOT.util.exception.error_code.UserErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NoteServiceImpl {

    private final NoteRoomRepository noteRoomRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteResponse sendNote(Long roomId, String senderEmail, String noteContent) {

        NoteRoom room = noteRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("NoteRoom not found"));

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND));

        log.info("Sender email: " + senderEmail, sender.getNickname());
        User receiver = room.getPost().getUser();

        Note newNote = Note.createNote(noteContent, sender, receiver, room);
        Note savedNote = noteRepository.save(newNote);
        log.info("Saved new note with ID: {}", savedNote.getId());

        return NoteResponse.builder().
                sender(savedNote.getSender().getNickname())
                .receiver(savedNote.getReceiver().getNickname())
                .noteContent(savedNote.getNoteContent())
                .sentAt(savedNote.getCreatedDate())
                .build();
    }
}
