package T2F2.SPOT.domain.post.controller;

import T2F2.SPOT.domain.category.entity.Category;
import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.SortBy;
import T2F2.SPOT.domain.post.dto.CreatePostDto;
import T2F2.SPOT.domain.post.dto.ModifyPostDto;
import T2F2.SPOT.domain.post.dto.QPostDto;
import T2F2.SPOT.domain.post.dto.responsePostDto;
import T2F2.SPOT.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

//    @GetMapping("api/posts")
//    public List<responsePostDto> getListPost(){
//        return postService.findAllPost();
//    }

    @GetMapping("api/post/{id}")
    public responsePostDto getDetailPost(@PathVariable("id") Long id) {
        return postService.findPostById(id);
    }

    @GetMapping("api/posts")
    public Slice<QPostDto> getSearchAndFilterAndSortPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) PostStatus postStatus,
            @RequestParam(required = false) PostFor postFor,
            @RequestParam(defaultValue = "0") String minPrice,
            @RequestParam(defaultValue = "1000000") String maxPrice,
            @RequestParam(required = false) SortBy sortBy,
            @RequestParam(defaultValue = "0") int startIndex
            ) {


        return postService.getSearchFilterList(keyword, category, postFor, postStatus, minPrice, maxPrice, sortBy, startIndex);
    }

    @PutMapping("api/post/updateStatus/{id}/{status}")
    public void updateStatus(
            @PathVariable("id") Long id,
            @PathVariable("status") String status
    ) {
        postService.updateStatus(id, status);
    }

    @PutMapping("api/post/modify/{id}")
    public void modifyPost(
            @PathVariable("id") Long id,
            @RequestBody ModifyPostDto modifyPostDto
    )
    {
        postService.modifyPost(id, modifyPostDto);
    }
}
