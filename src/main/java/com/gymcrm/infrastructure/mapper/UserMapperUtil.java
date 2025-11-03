package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.User;
import com.gymcrm.infrastructure.persistence.dao.UserDao;

/**
 * @author Alish
 */
abstract class UserMapperUtil {
    protected static UserDao getUserDao(User user) {
        return new UserDao(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.isActive());
    }
    protected static User getUser(UserDao dao) {
        return new User(dao.getUserName(), dao.getPassword(), dao.getFirstName(),
                dao.getLastName(), dao.isActive());
    }
}
