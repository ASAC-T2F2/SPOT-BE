package T2F2.SPOT.domain.note.controller;

import T2F2.SPOT.domain.note.dto.NoteRequest;
import T2F2.SPOT.domain.note.dto.NoteResponse;
import T2F2.SPOT.domain.note.service.NoteServiceImpl;
import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NoteController {

    private final NoteServiceImpl noteService;

    @MessageMapping("/room/{noteRoomId}")
    @SendTo("/sub/room/{noteRoomId}")
    public NoteResponse sendNote(@DestinationVariable("noteRoomId") Long noteRoomId, NoteRequest noteRequest) {

        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = customUserDetails.getUser();

            if(user != null) {
                return noteService.sendNote(noteRoomId, user.getNickname(), noteRequest);
            } else {
                log.warn("User is null in CustomUserDetails");
                throw new RuntimeException("User details are missing. Cannot send note.");
            }
        } else {
            log.warn("Authentication is null or invalid");
            throw new RuntimeException("Authentication failed. Cannot send note.");
        }
    }
}
