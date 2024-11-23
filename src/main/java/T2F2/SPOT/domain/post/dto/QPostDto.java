package T2F2.SPOT.domain.post.dto;

import T2F2.SPOT.domain.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QPostDto {
    Long id;
    String title;
    String imageUrl;
    int price;

    private QPostDto(Long id, String title, String imageUrl, int price) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
    }
    public static QPostDto of(Post post) {
        String url;
        if(post.getPostImages().isEmpty())
        {
            url = "";
        }
        else
        {
            url = post.getPostImages().get(0).getImageUrl();
        }
        return new QPostDto(post.getId(), post.getTitle(), url, post.getPrice());
    }


}