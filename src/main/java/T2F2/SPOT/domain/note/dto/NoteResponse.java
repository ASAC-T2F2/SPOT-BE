package T2F2.SPOT.domain.note.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("isSender") // JSON에 "isSender"로 출력되도록 변경
    private boolean isSender;

}
