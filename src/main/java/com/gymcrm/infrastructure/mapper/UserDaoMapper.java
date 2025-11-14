package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.User;
import com.gymcrm.infrastructure.dao.UserDao;

/**
 * @author Alish
 */
public class UserDaoMapper {

    private UserDaoMapper() {}

    public static UserDao toDao(User userProfile) {
        return new UserDao(userProfile.getId(), userProfile.getUsername(), userProfile.getPassword(),
                userProfile.getFirstName(), userProfile.getLastName(), userProfile.isActive());
    }
    public static User toDomain(UserDao dao) {
        return new User(dao.getId(), dao.getUsername(), dao.getPassword(), dao.getFirstName(),
                dao.getLastName(),  dao.isActive());
    }
}
