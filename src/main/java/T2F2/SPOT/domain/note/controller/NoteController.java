package T2F2.SPOT.domain.note.controller;

import T2F2.SPOT.domain.note.dto.NoteMessage;
import T2F2.SPOT.domain.note.entity.Note;
import T2F2.SPOT.domain.note.service.NoteServiceImpl;
import T2F2.SPOT.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NoteController {

    private final NoteServiceImpl noteService;

    @MessageMapping("{noteRoomId}")
    @SendTo("/room/{roomId}")
    public NoteMessage test(@DestinationVariable Long roomId, NoteMessage message) {

        Note note = noteService.createNote(roomId, message.getSender(), message.getNoteContent());
        return NoteMessage.builder()
                .roomId(roomId)
                .sender(note.getSender())
                .noteContent(note.getNoteContent())
                .build();
    }

}
