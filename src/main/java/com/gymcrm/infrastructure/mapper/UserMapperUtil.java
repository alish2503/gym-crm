package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.User;
import com.gymcrm.infrastructure.persistence.dao.UserDao;

/**
 * @author Alish
 */
public abstract class UserMapperUtil {
    protected static void mapToDomainBase(User user, UserDao dao) {
        user.setUsername(dao.getUsername());
        user.setPassword(dao.getPassword());
        user.setFirstName(dao.getFirstName());
        user.setLastName(dao.getLastName());
        user.setActive(dao.isActive());
    }
}
