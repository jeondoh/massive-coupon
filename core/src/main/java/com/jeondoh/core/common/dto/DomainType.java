package com.jeondoh.core.common.dto;

import com.jeondoh.core.common.util.StaticVariables;
import com.jeondoh.core.common.util.TimeHelper;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public enum DomainType {
    DEFAULT,
    COUPON;

    public String getConfigKey(String resourceId) {
        return String.format(StaticVariables.CONFIG_KEY + ":%s:%s", this.name(), resourceId);
    }

    public static String getDefaultConfigKey() {
        String name = DomainType.DEFAULT.name();
        return String.format(StaticVariables.CONFIG_KEY + ":%s:%s", name, name.toLowerCase());
    }

    public String getTrafficKey(String resourceId, LocalDateTime localDateTime) {
        String timeKey = TimeHelper.formatToTenSecondBucket(localDateTime);
        return String.format(StaticVariables.TRAFFIC_KEY + ":%s:%s:%s", this.name(), resourceId, timeKey);
    }

    public String getDomainKey(String resourceId, String memberId) {
        return this.name() + ":" + resourceId + ":" + memberId;
    }
}
