package T2F2.SPOT.domain.user.grade.strategy;

import T2F2.SPOT.domain.user.entity.User;

public interface GradeStrategy {
    boolean matches(User user);
}
