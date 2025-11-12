package repository;

import com.gymcrm.domain.model.User;
import com.gymcrm.infrastructure.mapper.UserMapper;
import com.gymcrm.infrastructure.persistence.dao.UserDao;
import com.gymcrm.infrastructure.repository.UserProfileRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class UserProfileRepositoryImplTest {

    @Mock
    EntityManager entityManager;

    @Mock
    TypedQuery<UserDao> userQuery;

    @Mock
    TypedQuery<Long> countQuery;

    @InjectMocks
    UserProfileRepositoryImpl repository;

    User domainUser;
    UserDao userDao;

    @BeforeEach
    void init() {
        domainUser = new User(1L, "john", "pass", "John", "Doe", true);
        userDao = UserMapper.toDao(domainUser);
    }

    @Test
    void findProfileByUserName_shouldReturnDomainUser() {
        when(entityManager.createQuery(anyString(), eq(UserDao.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), anyString())).thenReturn(userQuery);
        when(userQuery.getResultStream()).thenReturn(Stream.of(userDao));
        Optional<User> result = repository.findProfileByUserName("john");
        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
        verify(userQuery).setParameter("username", "john");
    }

    @Test
    void findProfileByUserName_shouldReturnEmptyIfNotFound() {
        when(entityManager.createQuery(anyString(), eq(UserDao.class))).thenReturn(userQuery);
        when(userQuery.setParameter(anyString(), anyString())).thenReturn(userQuery);
        when(userQuery.getResultStream()).thenReturn(Stream.empty());
        Optional<User> result = repository.findProfileByUserName("unknown");
        assertFalse(result.isPresent());
    }

    @Test
    void updateProfile_shouldMergeDao() {
        repository.updateProfile(domainUser);
        verify(entityManager).merge(argThat((UserDao dao) ->
                dao.getId().equals(domainUser.getId()) &&
                        dao.getUsername().equals(domainUser.getUsername()) &&
                        dao.getFirstName().equals(domainUser.getFirstName())
        ));
    }

    @Test
    void existsByUserName_shouldReturnTrue() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(anyString(), anyString())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);
        assertTrue(repository.existsByUserName("john"));
        verify(countQuery).setParameter("uName", "john");
    }

    @Test
    void existsByUserName_shouldReturnFalse() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(anyString(), anyString())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(0L);
        assertFalse(repository.existsByUserName("unknown"));
    }
}
