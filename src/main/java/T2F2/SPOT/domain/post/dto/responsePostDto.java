package T2F2.SPOT.domain.post.dto;

import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.wish.entity.Wish;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class responsePostDto {

    private String title;
    private String content;
    private String userNickname;
    private PostStatus postStatus;
    private String price;
    private int wish_count;
    private LocalDateTime post_date;

    public responsePostDto(String title, String content, User user, PostStatus postStatus, String price, int wish_count, LocalDateTime post_date) {
        this.title = title;
        this.content = content;
        this.userNickname = user.getNickname();
        this.postStatus = postStatus;
        this.price = price;
        this.wish_count = wish_count;
        this.post_date = post_date;
    }

    public responsePostDto(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.userNickname = user.getNickname();
    }

    public static responsePostDto of(Post post) {

        return new responsePostDto(
                post.getTitle(),
                post.getContent(),
                post.getUser(),
                post.getPostStatus(),
                post.getPrice(),
                post.getWishes().size(),
                post.getCreatedDate()
        );
    }
}
