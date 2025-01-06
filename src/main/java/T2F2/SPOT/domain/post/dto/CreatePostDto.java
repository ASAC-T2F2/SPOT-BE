package T2F2.SPOT.domain.post.dto;

import T2F2.SPOT.domain.post.Category;
import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.entity.PostImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CreatePostDto {

//    private Long userId;
    private String title;
    private String content;
    private PostFor postFor;
//    private PostStatus postStatus;
    private int price;
    private Category category;
    private List<String> imageUrls;
}