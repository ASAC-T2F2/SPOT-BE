package T2F2.SPOT.domain.user.grade.strategy;

import T2F2.SPOT.domain.user.entity.User;

public class DoctorStrategy implements GradeStrategy {

    // 20개 이상의 거래 완료 게시글 + 매너점수가 4.0 이상인 사용자
    @Override
    public boolean matches(User user) {
        return user.getCompletedPostCount() >= 20 && user.getMannerScore() >= 4.0;
    }
}
