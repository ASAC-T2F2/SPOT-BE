package T2F2.SPOT.domain.post.controller;

import T2F2.SPOT.domain.category.entity.Category;
import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.SortBy;
import T2F2.SPOT.domain.post.dto.*;
import T2F2.SPOT.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post/create")
    @Operation(summary = "게시글 생성", description = "입력 값을 받아 게시글을 최초 생성해주는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public void createPost(
            @RequestBody CreatePostDto createPostDto
    ) {
        postService.createPost(createPostDto);
    }


    @GetMapping("/posts")
    @Operation(summary = "전체 게시글 목록 반환", description = "전체 모든 게시글 목록을 반환하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of posts returned successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<PostResponse> getListPost(){
        return postService.findAllPost();
    }


    @GetMapping("/posts/purpose")
    @Operation(summary = "팔래요/살래요 게시글 목록 반환", description = "팔래요/살래요 게시글 목록을 반환하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔래요/살래요 게시글 목록 반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 에러")
    })
    public ResponseEntity<PostListWithPagination> getPostsByPurpose(@RequestParam int limit, @RequestParam(required = false) Long lastPostId, @RequestParam PostFor postFor) {
        PostListWithPagination resultPosts = postService.findPostsForPurpose(limit, lastPostId == null ? Long.MAX_VALUE : lastPostId, postFor);
        return new ResponseEntity<>(resultPosts, HttpStatus.OK);
    }


    @GetMapping("/post/{id}")
    @Operation(summary = "단일 게시글 정보 반환", description = "특정 게시글 클릭 시, 해당 게시글 데이터를 반환하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post details returned successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getDetailPost(@PathVariable("id") Long id) {
        PostResponse result = postService.getPostDetail(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/posts/querydsl")
    @Operation(summary = "게시글 목록 필터 검색", description = "검색어, 카테고리, 게시글 상태, 게시글 목적, 금액범위, 정렬 등을 받아 필터링 된 게시글 목록을 반환하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered and sorted posts returned successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Slice<QPostDto> getSearchAndFilterAndSortPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) PostStatus postStatus,
            @RequestParam(required = false) PostFor postFor,
            @RequestParam(defaultValue = "0") int minPrice,
            @RequestParam(defaultValue = "1000000") int maxPrice,
            @RequestParam(required = false) SortBy sortBy,
            @RequestParam(defaultValue = "0") int startIndex
            ) {
        return postService.getSearchFilterList(keyword, category, postFor, postStatus, minPrice, maxPrice, sortBy, startIndex);
    }


    @GetMapping("/post/feed/major/{major}")
    @Operation(summary = "전공 피드", description = "전공 별, 게시글 목록을 반환하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts filtered by major returned successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<QPostDto> getPostFilterByMajor(
            @PathVariable("major") String major){

        return postService.findPostByMajor(major);
    }


    @GetMapping("/post/feed/user/{userId}")
    @Operation(summary = "내가 올린 피드", description = "내가 올린 게시글 목록을 반환하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts filtered by user ID returned successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<QPostDto> getPostFilterByUserId(
            @PathVariable("userId") Long userId
    ) {
        return postService.findPostByUserId(userId);
    }


    @PutMapping("/post/updateStatus/{id}/{status}")
    @Operation(summary = "글 상태 변경", description = "게시글의 상태를 판매중, 판매완료 등의 상태로 변경하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status value"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void updateStatus(
            @PathVariable("id") Long id,
            @PathVariable("status") String status
    ) {
        postService.updateStatus(id, status);
    }


    @PutMapping("/post/modify/{id}")
    @Operation(summary = "게시글 수정", description = "게시글의 제목, 내용, 가격을 수정할 수 있는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post modified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Post not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void modifyPost(
            @PathVariable("id") Long id,
            @RequestBody ModifyPostDto modifyPostDto
    )
    {
        postService.modifyPost(id, modifyPostDto);
    }
}