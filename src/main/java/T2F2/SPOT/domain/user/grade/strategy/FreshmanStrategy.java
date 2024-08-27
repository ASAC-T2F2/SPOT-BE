package T2F2.SPOT.domain.user.grade.strategy;

import T2F2.SPOT.domain.user.entity.User;

public class FreshmanStrategy implements GradeStrategy {

    @Override
    public boolean matches(User user) {

        // 모든 회원의 기본 등급
        return true;
    }
}
