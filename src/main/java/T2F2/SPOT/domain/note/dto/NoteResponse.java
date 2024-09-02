package T2F2.SPOT.domain.note.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NoteResponse {

    private String sender;
    private String noteContent;
}
