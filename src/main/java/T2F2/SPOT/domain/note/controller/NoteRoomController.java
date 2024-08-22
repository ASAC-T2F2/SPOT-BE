package T2F2.SPOT.domain.note.controller;

import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.note.service.NoteServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/chatRoom")
public class NoteRoomController {

    private final NoteServiceImpl noteService;

    @GetMapping("/roomList")
    public List<NoteRoom> roomList() {
        return noteService.findAllRoom();
    }

}
