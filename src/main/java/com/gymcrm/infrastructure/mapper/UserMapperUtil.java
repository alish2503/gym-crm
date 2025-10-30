package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.User;
import com.gymcrm.infrastructure.persistence.dao.UserDao;

/**
 * @author Alish
 */
public abstract class UserMapperUtil {
    protected static UserDao getUserDao(User user) {
        return new UserDao(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.isActive());
    }
    protected static void mapToDomainBase(User user, UserDao dao) {
        user.setUsername(dao.getUserName());
        user.setPassword(dao.getPassword());
        user.setFirstName(dao.getFirstName());
        user.setLastName(dao.getLastName());
        user.setActive(dao.isActive());
    }
}
