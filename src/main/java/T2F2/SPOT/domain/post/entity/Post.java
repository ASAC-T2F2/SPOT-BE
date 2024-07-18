package T2F2.SPOT.domain.post.entity;

import T2F2.SPOT.domain.category.entity.Category;
import T2F2.SPOT.domain.rank.entity.Rank;
import T2F2.SPOT.domain.review.entity.Review;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.wish.entity.Wish;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String post_title;

    private String post_content;


    // ENUM 생성해야 함
    // 게시물 목적
    // 게시물 상태

    // 등록일
    // 수정일

    private String price;

    private Boolean is_deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post_image")
    private List<Post_Image> postImages = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @OneToOne(mappedBy = "review")
    private Review review;

    @OneToMany(mappedBy = "wish")
    private List<Wish> wishes = new ArrayList<>();
}
