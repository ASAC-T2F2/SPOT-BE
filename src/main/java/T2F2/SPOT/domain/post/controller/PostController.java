package T2F2.SPOT.domain.post.controller;

import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.dto.CreatePostDto;
import T2F2.SPOT.domain.post.dto.responsePostDto;
import T2F2.SPOT.domain.post.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("api/post/create")
    public void createPost(
            @RequestBody CreatePostDto createPostDto
    ) {
        postService.createPost(createPostDto);
    }

    @GetMapping("api/posts")
    public List<responsePostDto> getListPost(){
        return postService.findAllPost();
    }

    @GetMapping("api/post/{id}")
    public responsePostDto getDetailPost(@PathVariable("id") Long id) {
        return postService.findPostById(id);
    }

    @PutMapping("api/post/updateStatus/{id}/{status}")
    public void updateStatus(
            @PathVariable("id") Long id,
            @PathVariable("status") PostStatus status
    ) {
        postService.updateStatus(id, status);
    }

}
