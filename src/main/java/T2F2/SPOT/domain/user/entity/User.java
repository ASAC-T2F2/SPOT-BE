package T2F2.SPOT.domain.user.entity;

import T2F2.SPOT.domain.note.entity.Note;
import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.review.entity.Review;
import T2F2.SPOT.domain.user.Role;
import T2F2.SPOT.domain.user.grade.Grade;
import T2F2.SPOT.domain.wish.entity.Wish;
import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String nickname;
    private String university;
    private String major;
    private String entranceYear;
    private Boolean isDeleted;
    private String imageUrl;
    private float mannerScore;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Grade grade;

    // 생성 일자
    // 탈퇴 일자

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wish> wishes = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Review> sendReviews = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<Review> receivedReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Note> notes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<NoteRoom> noteRooms = new ArrayList<>();

    /* 매너온도 업데이트 관련 로직*/
    /**
     * 매너온도 업데이트
     * @param reviewScore
     * @return 업데이트 적용 된 매너점수
     */
    public float updateMannerScore(float reviewScore) {

        float roundedReviewScore = Math.round(reviewScore * 10) / 10.0f;
        this.mannerScore = this.mannerScore + (roundedReviewScore - this.mannerScore) / 10;
        this.mannerScore = Math.round(this.mannerScore * 10) / 10.0f;

        return this.mannerScore;
    }

    /* 등급 체크 및 업데이트 관련 로직*/
    /**
     * 사용자의 거래완료 게시글 카운트
     * @return 거래완료 상태인 게시글 개수
     */
    public int getCompletedPostCount() {
        return (int) posts.stream()
                .filter(post -> post.getPostStatus() == PostStatus.TRADE_COMPLETE)
                .count();
    }

    /**
     * 사용자의 거래중 게시글 카운트
     * @return 거래중 상태인 게시글 개수
     */
    public int getTradingPostCount() {
        return (int) posts.stream()
                .filter(post -> post.getPostStatus() == PostStatus.TRADING)
                .count();
    }

    /**
     * 현재 사용자의 등급 평가
     * @return 평가 완료된 최종 등급
     */
    public Grade getGrade() {
        if (this.grade == null | !this.grade.matchesCondition(this)) {
            this.grade = Grade.evaluateGrade(this);
        }
        return this.grade;
    }
}

