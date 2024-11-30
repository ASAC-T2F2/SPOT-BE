package T2F2.SPOT.domain.note.service;

import T2F2.SPOT.domain.note.dto.NoteResponse;
import T2F2.SPOT.domain.note.dto.NoteRoomRequestDto;
import T2F2.SPOT.domain.note.dto.NoteRoomResponseDto;
import T2F2.SPOT.domain.note.entity.Note;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.note.repository.NoteRepository;
import T2F2.SPOT.domain.note.repository.NoteRoomRepository;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.post.repository.PostRepository;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.repository.UserRepository;
import T2F2.SPOT.domain.user.service.AuthService;
import T2F2.SPOT.util.exception.CustomException;
import T2F2.SPOT.util.exception.error_code.NoteErrorCode;
import T2F2.SPOT.util.exception.error_code.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteRoomServiceImpl {

    private final NoteRoomRepository noteRoomRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional(readOnly = true)
    public List<NoteRoom> findNoteRoomsForLoginUser(String guestEmail) {
        return noteRoomRepository.findByGuestEmail(guestEmail);
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        String requestEmail = authService.getAuthenticatedUserEmail();
        User requester = userRepository.findByEmail(requestEmail);

        if(requester == null) {
            throw new CustomException(UserErrorCode.NOT_FOUND);
        }

        NoteRoom room = noteRoomRepository.findById(roomId).orElseThrow(() -> new CustomException(NoteErrorCode.NOT_FOUND));

        if(!room.getPost().getUser().equals(requester) && !room.getGuest().equals(requester)) {
            throw new CustomException(NoteErrorCode.DELETE_NOT_AUTHORIZED);
        }

        noteRoomRepository.deleteById(roomId);
    }

    public NoteRoomResponseDto createRoom(NoteRoomRequestDto noteRoomRequestDto, String sender) {

        User guest = userRepository.findByEmail(sender);
        if (guest == null) {
            throw new CustomException(UserErrorCode.NOT_FOUND);
        }
        Post post = postRepository.findById(noteRoomRequestDto.getPostId()).orElseThrow();

        NoteRoom existingRoom = noteRoomRepository.findByPostAndGuest(post, guest);
        if(existingRoom != null) {
            return  NoteRoomResponseDto.builder()
                    .roomId(existingRoom.getId())
                    .postId(existingRoom.getPost().getId())
                    .guest(existingRoom.getGuest().getNickname())
                    .build();
        }

        NoteRoom newRoom = NoteRoom.createRoom(post, guest);
        NoteRoom savedRoom = noteRoomRepository.save(newRoom);
        return NoteRoomResponseDto.builder()
                .roomId(savedRoom.getId())
                .postId(savedRoom.getPost().getId())
                .guest(savedRoom.getGuest().getNickname())
                .build();
    }

    public List<NoteResponse> enterRoom(Long roomId, String userEmail) {

        NoteRoom room = noteRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(NoteErrorCode.NOT_FOUND));

//        Post post = room.getPost();

        List<NoteResponse> notes = noteRepository.findAllByNoteRoomId(roomId).stream()
                .map(note -> NoteResponse.builder()
                        .sender(note.getSender().getNickname())
                        .receiver(note.getReceiver().getNickname())
                        .noteContent(note.getNoteContent())
                        .sentAt(note.getCreatedDate())
                        .isSender(userEmail.equals(note.getSender().getEmail()))
                        .build())
                .collect(Collectors.toList());

        return notes;

    }

}
