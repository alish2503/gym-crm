package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.User;
import com.gymcrm.infrastructure.persistence.dao.UserDao;

/**
 * @author Alish
 */
public abstract class UserMapperUtil {

    protected static void mapToDaoBase(User user, UserDao dao) {
        dao.setUsername(user.getUsername());
        dao.setPassword(user.getPassword());
        dao.setFirstName(user.getFirstName());
        dao.setLastName(user.getLastName());
        dao.setActive(user.isActive());
    }
}
