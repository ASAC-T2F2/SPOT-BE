package T2F2.SPOT.domain.post.dto;

import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
public class SliceSearchPostResponse<T> {
    private final List<T> posts;
    private final boolean hasNext;

    public SliceSearchPostResponse(Slice<T> slice) {
        this.posts = slice.getContent();
        this.hasNext = slice.hasNext();
    }
}
