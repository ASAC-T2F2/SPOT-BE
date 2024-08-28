package T2F2.SPOT.domain.user.dto;

import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.grade.dto.GradeInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyProfileResponse {

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

    public static MyProfileResponse from(User user) {
        return new MyProfileResponse(user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getUniversity(),
                user.getMajor(),
                user.getEntranceYear(),
                user.getImageUrl(),
                user.getMannerScore(),
                user.getCompletedPostCount(),
                user.getTradingPostCount(),
                GradeInfo.from(user.getGrade())
        );
    }
}
