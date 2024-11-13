package T2F2.SPOT.domain.user.grade.strategy;

import T2F2.SPOT.domain.user.entity.User;

public class BachelorStrategy implements GradeStrategy {

    // 5개 이상의 거래 완료 게시글을 보유한 사용자
    @Override
    public boolean matches(User user) {
        return user.getCompletedPostCount() >= 5;
    }
}
