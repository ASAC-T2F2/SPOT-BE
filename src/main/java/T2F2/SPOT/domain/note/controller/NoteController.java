package T2F2.SPOT.domain.note.controller;

import T2F2.SPOT.domain.note.dto.NoteRequest;
import T2F2.SPOT.domain.note.dto.NoteResponse;
import T2F2.SPOT.domain.note.service.NoteServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NoteController {

    private final NoteServiceImpl noteService;

    @MessageMapping("/room/{noteRoomId}")
    @SendTo("/sub/room/{noteRoomId}")
    public NoteResponse sendNote(@DestinationVariable Long noteRoomId,
                                 @Payload NoteRequest noteRequest,
                                 SimpMessageHeaderAccessor accessor) {

        String email = (String) accessor.getSessionAttributes().get("senderEmail");

        log.info("이메일 찾기: {}", email);
        return noteService.sendNote(noteRoomId, email, noteRequest);
    }
}
