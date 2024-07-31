package T2F2.SPOT.domain.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ModifyPostDto {

    String title;
    String content;
    String price;
}
