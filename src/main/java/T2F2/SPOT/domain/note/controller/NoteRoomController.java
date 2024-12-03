package T2F2.SPOT.domain.note.controller;

import T2F2.SPOT.domain.note.dto.NoteResponse;
import T2F2.SPOT.domain.note.dto.NoteRoomRequestDto;
import T2F2.SPOT.domain.note.dto.NoteRoomResponseDto;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.note.service.NoteRoomServiceImpl;
import T2F2.SPOT.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/noteRoom")
public class NoteRoomController {

    private final NoteRoomServiceImpl noteRoomService;

    //로그인한 유저의 채팅방 목록 조회
    @GetMapping("/roomList")
    public ResponseEntity<List<NoteRoomResponseDto>> getAllRoomsForLoginUser() {

        List<NoteRoom> rooms = noteRoomService.findNoteRoomsForLoginUser();
        List<NoteRoomResponseDto> response = rooms.stream()
                .map(room -> NoteRoomResponseDto.builder()
                        .roomId(room.getId())
                        .build())
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //채팅방 삭제
    @DeleteMapping("/deleteRoom/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long roomId) {
        noteRoomService.deleteRoom(roomId);

        return new ResponseEntity<>("채팅방 삭제 성공", HttpStatus.OK);
    }


    //채팅방 개설
    @PostMapping("/createRoom")
    public ResponseEntity<?> createRoom(@RequestBody NoteRoomRequestDto noteRoomRequestDto) {

        NoteRoomResponseDto responseDto = noteRoomService.createRoom(noteRoomRequestDto);
        log.info("Create Note Room, Post ID: {}, Receiver: {}", responseDto.getPostId(), responseDto.getGuest());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }


    //채팅방 입장
    @GetMapping("/enterRoom/{roomId}")
    public ResponseEntity<List<NoteResponse>> enterRoom(@PathVariable Long roomId) {
        List<NoteResponse> responses = noteRoomService.enterRoom(roomId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

}
