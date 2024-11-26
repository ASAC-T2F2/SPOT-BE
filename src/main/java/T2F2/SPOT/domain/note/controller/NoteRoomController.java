package T2F2.SPOT.domain.note.controller;

import T2F2.SPOT.domain.note.dto.NoteRoomRequestDto;
import T2F2.SPOT.domain.note.dto.NoteRoomResponseDto;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.note.service.NoteRoomServiceImpl;
import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/noteRoom")
public class NoteRoomController {

    private final NoteRoomServiceImpl noteRoomService;
    private final AuthService authService;

    //로그인한 유저의 채팅방 목록 조회
    @GetMapping("/roomList")
    public ResponseEntity<List<NoteRoomResponseDto>> getAllRoomsForLoginUser() {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.emptyList());
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
         String LoginUserEmail = userDetails.getUsername();

        log.info("로그인 유저 메일 확인" + LoginUserEmail);
        List<NoteRoom> rooms = noteRoomService.findNoteRoomsForLoginUser(LoginUserEmail);
        List<NoteRoomResponseDto> response = rooms.stream()
                .map(room -> NoteRoomResponseDto.builder()
                        .roomId(room.getId())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteRoom/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomId) {
        noteRoomService.deleteRoom(roomId);

        return new ResponseEntity<>("채팅방 삭제 성공", HttpStatus.OK);
    }
//
//    //게시글의 채팅방 조회?
//    @GetMapping("/room/{postId}")
//    public ResponseEntity<NoteRoom> getRoomById(@PathVariable Long postId) {
//        log.info("Get Room by postId: {}", postId);
//        NoteRoom room = noteRoomService.findRoomById(postId);
//        return ResponseEntity.ok(room);
//    }

    //채팅방 개설
    @PostMapping("/createRoom")
    public ResponseEntity<?> createRoom(@RequestBody NoteRoomRequestDto noteRoomRequestDto) {
        String userEmail = authService.getAuthenticatedUserEmail();

        NoteRoomResponseDto responseDto = noteRoomService.createRoom(noteRoomRequestDto, userEmail);
        log.info("Create Note Room, Post ID: {}, Receiver: {}", responseDto.getPostId(), responseDto.getGuest());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }


}
