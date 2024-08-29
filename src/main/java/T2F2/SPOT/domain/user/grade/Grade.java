package T2F2.SPOT.domain.user.grade;

import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.grade.strategy.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Grade {

    FRESHMAN("새내기", "/images/freshman.png", new FreshmanStrategy()),
    UNDERGRADUATE("학부생", "/images/undergraduate.png", new UndergraduateStrategy()),
    BACHELOR("학사", "/images/bachelor.png", new BachelorStrategy()),
    MASTER("석사", "/images/master.png", new MasterStrategy()),
    DOCTOR("박사", "/images/doctor.png", new DoctorStrategy()),
    PROFESSOR("교수", "/images/professor.png", new ProfessorStrategy());

    @Getter
    private final String rankName;

    @Getter
    private final String rankImageUrl;

    private final GradeStrategy strategy;


    /**
     * 현재 사용자의 등급 검증
     * @param user
     * @return 일치 | 불일치
     */
    public boolean matchesCondition(User user) {
        return strategy.matches(user);
    }


    /**
     * 현재 사용자의 등급 평가
     * @param user
     * @return 현재 사용자의 등급
     */
    public static Grade evaluateGrade(User user) {

        // 낮은 등급부터 순서대로 평가
        for (Grade grade : Grade.values()) {
            if (grade.matchesCondition(user)) {
                return grade;
            }
        }
        return FRESHMAN;
    }
}
