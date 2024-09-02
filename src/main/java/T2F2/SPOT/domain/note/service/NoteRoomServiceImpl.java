package T2F2.SPOT.domain.note.service;

import T2F2.SPOT.domain.note.dto.NoteRoomRequestDto;
import T2F2.SPOT.domain.note.dto.NoteRoomResponseDto;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.note.repository.NoteRoomRepository;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.post.repository.PostRepository;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import T2F2.SPOT.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteRoomServiceImpl {

    private final NoteRoomRepository noteRoomRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<NoteRoom> findNoteRoomsForLoginUser(String guestEmail) {
        return noteRoomRepository.findByGuestEmail(guestEmail);
    }

    @Transactional
    public void deleteRoom(Long roomId, String requestEmail) {
        User requester = userRepository.findByEmail(requestEmail);
        if(requester == null) {
            throw new UserExceptions.UserNotFoundException(requestEmail);
        }

        NoteRoom room = noteRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found: " + roomId));

        if(!room.getOwner().equals(requester) && !room.getGuest().equals(requester)) {
            throw new RuntimeException("User is not authorized to delete this room");
        }

        noteRoomRepository.deleteById(roomId);
    }
//    @Transactional(readOnly = true)
//    public NoteRoom findRoomById(Long id) {
//        return noteRoomRepository.findById(id).orElseThrow();
//    }

    public NoteRoomResponseDto createRoom(NoteRoomRequestDto noteRoomRequestDto, String sender) {

        User guest = userRepository.findByEmail(sender);
        if (guest == null) {
            throw new UserExceptions.UserNotFoundException("User not found: " + sender);
        }
        Post post = postRepository.findById(noteRoomRequestDto.getPostId()).orElseThrow();

        User owner = post.getUser();

        NoteRoom existingRoom = noteRoomRepository.findByPostAndGuest(post, guest);
        if(existingRoom != null) {
            return  NoteRoomResponseDto.builder()
                    .roomId(existingRoom.getId())
                    .postId(existingRoom.getPost().getId())
                    .owner(existingRoom.getOwner().getNickname())
                    .guest(existingRoom.getGuest().getNickname())
                    .build();
        }

        NoteRoom newRoom = NoteRoom.createRoom(post, owner, guest);
        NoteRoom savedRoom = noteRoomRepository.save(newRoom);
        return NoteRoomResponseDto.builder()
                .roomId(savedRoom.getId())
                .postId(savedRoom.getPost().getId())
                .owner(savedRoom.getOwner().getNickname())
                .guest(savedRoom.getGuest().getNickname())
                .build();
    }


}
