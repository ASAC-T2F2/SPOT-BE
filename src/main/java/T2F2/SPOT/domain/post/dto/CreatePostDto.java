package T2F2.SPOT.domain.post.dto;

import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.PostStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreatePostDto {

    private Long userId;
    private String title;
    private String content;
    private PostFor postFor;
    private PostStatus postStatus;
    private String price;
}
