package T2F2.SPOT.domain.user.dto.profile;

import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.grade.dto.GradeInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseProfileResponse {

    private Long userId;
    private String email;
    private String nickname;
    private String university;
    private String major;
    private String entranceYear;
    private String profileImageUrl;
    private float mannerScore;
    private int completedPostCount;
    private int tradingPostCount;

    private GradeInfo grade;

    protected BaseProfileResponse(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.university = user.getUniversity();
        this.major = user.getMajor();
        this.entranceYear = user.getEntranceYear();
        this.profileImageUrl = user.getImageUrl();
        this.mannerScore = user.getMannerScore();
        this.grade = GradeInfo.from(user.getGrade());
    }
}
