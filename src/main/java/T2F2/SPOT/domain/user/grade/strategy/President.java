package T2F2.SPOT.domain.user.grade.strategy;

import T2F2.SPOT.domain.user.entity.User;

public class President implements GradeStrategy {

    @Override
    public boolean matches(User user) {
        return user.getRole().equals("ADMIN");
    }
}
