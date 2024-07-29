package T2F2.SPOT.domain.post.service;

import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.dto.CreatePostDto;
import T2F2.SPOT.domain.post.dto.responsePostDto;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.post.repository.PostRepository;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void createPost(CreatePostDto createPostDto) {
        User findUser = userRepository.findById(createPostDto.getUserId()).orElseThrow();
        Post result = postRepository.save(Post.of(createPostDto, findUser));

    }

    @Transactional(readOnly = true)
    public List<responsePostDto> findAllPost() {
        return postRepository.findAll()
                .stream()
                .map(responsePostDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public responsePostDto findPostById(Long id) {
        Post reuslt = postRepository.findById(id).orElseThrow();
        return responsePostDto.of(reuslt);
    }

    public void updateStatus(Long id, PostStatus status) {
        Post findPost = postRepository.findById(id).orElseThrow();
        findPost.updatePostStatus(status);

    }
}