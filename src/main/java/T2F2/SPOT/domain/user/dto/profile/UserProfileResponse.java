package T2F2.SPOT.domain.user.dto.profile;

import T2F2.SPOT.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserProfileResponse extends BaseProfileResponse{

    private int completedPostCount;
    private int tradingPostCount;

    private UserProfileResponse(User user) {
        super(user);
        this.completedPostCount = user.getCompletedPostCount();
        this.tradingPostCount = user.getTradingPostCount();
    }

    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(user);
    }
}
