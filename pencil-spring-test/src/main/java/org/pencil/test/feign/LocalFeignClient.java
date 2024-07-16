package org.pencil.test.feign;

import org.pencil.beans.resp.Result;
import org.pencil.feign.config.ComFeignConfig;
import org.pencil.feign.decoder.FeignErrorDecoder;
import org.pencil.test.beans.resp.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The interface Local feign client.
 *
 * @author pencil
 * @Date 24 /07/16
 */
@FeignClient(name = "LocalFeignClient", url = "${self-feign.url.local}",
        configuration = {ComFeignConfig.class, FeignErrorDecoder.class})
public interface LocalFeignClient {

    /**
     * Gets user dto.
     *
     * @param id the id
     * @return the user dto
     */
    @GetMapping("/demo/mock")
    Result<UserDto> getUserDto(@RequestParam("id") String id);

}
