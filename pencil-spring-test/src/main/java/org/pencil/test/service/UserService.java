package org.pencil.test.service;

import org.pencil.test.entity.dto.UserDto;
import org.pencil.test.entity.resp.BiliNewsResp;

/**
 * The interface User service.
 *
 * @author pencil
 * @Date 24 /07/01
 */
public interface UserService {

    /**
     * Gets user by id.
     *
     * @param userId the user id
     * @return the user by id
     */
    UserDto getUserById(String userId);

    /**
     * Gets bili news.
     *
     * @param time the time
     * @return the bili news
     */
    BiliNewsResp getBiliNews(String time);

}
