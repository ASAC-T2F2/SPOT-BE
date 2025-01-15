package T2F2.SPOT.domain.post.dto;

import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.post.entity.PostImage;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class PostDetail extends PostResponse {

    @JsonProperty("isAuthor")
    private final boolean isAuthor;

    @JsonProperty("images")
    private final List<String> imageUrls;

    private PostDetail(Post post, boolean isAuthor, List<PostImage> images) {
        super(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser(),
                post.getPostStatus(),
                post.getPrice(),
                post.getWishes().size(),
                post.getCreatedDate(),
                post.getViewCount(),
                images.isEmpty() ? "default" : images.get(0).getImageUrl()
        );
        this.isAuthor = isAuthor;
        this.imageUrls = images.stream()
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());
    }

    public static PostDetail of(Post post, boolean isAuthor, List<PostImage> images) {
        return new PostDetail(post, isAuthor, images);
    }

    // @Getter와 @JasonProperty의 충돌 + isAuthor 필드명 이슈로 Getter 직접 작성
    public boolean getIsAuthor() {
        return isAuthor;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
