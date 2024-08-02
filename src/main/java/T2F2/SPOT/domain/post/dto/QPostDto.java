package T2F2.SPOT.domain.post.dto;

import T2F2.SPOT.domain.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QPostDto {
    Long id;
    String title;
    String price;

    private QPostDto(Long id, String title, String price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }
    public static QPostDto of(Post post) {
        return new QPostDto(post.getId(), post.getTitle(), post.getPrice());
    }


}
