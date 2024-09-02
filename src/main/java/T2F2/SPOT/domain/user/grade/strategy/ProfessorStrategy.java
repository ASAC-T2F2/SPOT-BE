package T2F2.SPOT.domain.user.grade.strategy;

import T2F2.SPOT.domain.user.entity.User;

public class ProfessorStrategy implements GradeStrategy {

    // 50번 이상의 거래 완료 게시글 + 매너점수 4.3 이상인 사용자
    @Override
    public boolean matches(User user) {
        return user.getCompletedPostCount() >= 50 && user.getMannerScore() >= 4.3;
    }
}
