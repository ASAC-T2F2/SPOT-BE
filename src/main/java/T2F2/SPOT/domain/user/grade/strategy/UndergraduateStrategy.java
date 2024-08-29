package T2F2.SPOT.domain.user.grade.strategy;

import T2F2.SPOT.domain.user.entity.User;

public class UndergraduateStrategy implements GradeStrategy{


    @Override
    public boolean matches(User user) {

        // 한 개 이상의 거래완료 게시글을 보유한 사용자인가?
        return user.getCompletedPostCount() >= 1;
    }
}
