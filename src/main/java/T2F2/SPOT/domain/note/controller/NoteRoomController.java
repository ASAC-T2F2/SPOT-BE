package T2F2.SPOT.domain.note.controller;

import T2F2.SPOT.domain.note.dto.NoteRoomDto;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.note.service.NoteServiceImpl;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/noteRoom")
public class NoteRoomController {

    private final NoteServiceImpl noteService;
    private final Us

    //채팅방 목록 조회
    @GetMapping("/roomList")
    public ResponseEntity<List<NoteRoom>> getAllRooms() {
        log.info("All Chat Rooms");
        List<NoteRoom> rooms = noteService.findAllRoom();
        return ResponseEntity.ok(rooms);
    }

    //특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    public ResponseEntity<NoteRoom> getRoomById(@PathVariable Long roomId) {
        log.info("Get Room by Id: {}", roomId);
        NoteRoom room = noteService.findRoomById(roomId);
        return ResponseEntity.ok(room);
    }

    //채팅방 개설
    @PostMapping("/room")
    public ResponseEntity<NoteRoom> createRoom(@RequestParam Long postId,
                                               @RequestParam String senderNickname,
                                               @RequestParam String receiverNickname) {
        log.info("Create Note Room, Post ID: {}, Sender: {}, Receiver: {}", postId, senderNickname, receiverNickname);

        User sender =
    }

}
