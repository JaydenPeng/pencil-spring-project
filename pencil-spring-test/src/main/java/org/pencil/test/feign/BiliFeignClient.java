package org.pencil.test.feign;

import org.pencil.feign.config.ComFeignConfig;
import org.pencil.test.beans.resp.BiliNewsResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author pencil
 * @Date 24/06/29
 */
@FeignClient(name = "BiliFeignClient", url = "${self-feign.url.bili}", configuration = ComFeignConfig.class)
public interface BiliFeignClient {

    @GetMapping("/api/bilibili-rs/")
    BiliNewsResp getNewsList();

}
