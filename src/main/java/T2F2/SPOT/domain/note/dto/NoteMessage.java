package T2F2.SPOT.domain.note.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class NoteMessage {

    private Long roomId;
    private String sender;
    private String noteContent;
}
