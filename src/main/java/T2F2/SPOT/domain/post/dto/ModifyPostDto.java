package T2F2.SPOT.domain.post.dto;

import T2F2.SPOT.domain.post.Category;
import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ModifyPostDto {

    private String title;
    private String content;
    private PostFor postFor;
    private PostStatus postStatus;
    private int price;
    private Category category;

}