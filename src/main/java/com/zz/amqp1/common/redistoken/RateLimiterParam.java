package com.zz.amqp1.common.redistoken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-04-16
 * Time: 6:08 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RateLimiterParam {

    private String name;

    private String app;

    private Integer maxPermits;

    private Integer rate;
}
