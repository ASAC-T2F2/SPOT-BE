package T2F2.SPOT.domain.note.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NoteRoomDto {

    private Long postId;
    private String sender;
    private String receiver;
}
