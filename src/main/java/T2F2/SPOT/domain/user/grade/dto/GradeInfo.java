package T2F2.SPOT.domain.user.grade.dto;

import T2F2.SPOT.domain.user.grade.Grade;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GradeInfo {

    private String name;
    private String imageUrl;

    public static GradeInfo from(Grade grade) {
        return new GradeInfo(grade.getRankName(), grade.getRankImageUrl());
    }
}
