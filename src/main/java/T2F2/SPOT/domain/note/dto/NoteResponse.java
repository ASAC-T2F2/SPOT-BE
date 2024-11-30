package T2F2.SPOT.domain.note.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NoteResponse {

    private String sender;
    private String receiver;
    private String noteContent;
    private LocalDateTime sentAt;
    private boolean isSender;

}
