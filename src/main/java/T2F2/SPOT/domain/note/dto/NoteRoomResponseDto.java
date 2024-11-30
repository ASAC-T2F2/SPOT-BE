package T2F2.SPOT.domain.note.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteRoomResponseDto {

    private Long roomId;
    private Long postId;
    private String guest;

}
