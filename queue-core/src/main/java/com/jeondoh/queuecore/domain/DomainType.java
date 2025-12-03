package com.jeondoh.queuecore.domain;

import com.jeondoh.queuecore.utils.TimeHelper;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.jeondoh.queuecore.utils.StaticVariables.CONFIG_KEY;
import static com.jeondoh.queuecore.utils.StaticVariables.TRAFFIC_KEY;

@Getter
public enum DomainType {
    DEFAULT,
    COUPON;

    public String getConfigKey(String resourceId) {
        return String.format(CONFIG_KEY + ":%s:%s", this.name(), resourceId);
    }

    public static String getDefaultConfigKey() {
        String name = DomainType.DEFAULT.name();
        return String.format(CONFIG_KEY + ":%s:%s", name, name.toLowerCase());
    }

    public String getTrafficKey(String resourceId, LocalDateTime localDateTime) {
        String timeKey = TimeHelper.formatToTenSecondBucket(localDateTime);
        return String.format(TRAFFIC_KEY + ":%s:%s:%s", this.name(), resourceId, timeKey);
    }
}
