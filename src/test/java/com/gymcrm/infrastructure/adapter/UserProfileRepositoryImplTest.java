package com.gymcrm.infrastructure.adapter;

import com.gymcrm.domain.model.User;
import com.gymcrm.infrastructure.persistence.adapter.UserProfileRepositoryImpl;
import com.gymcrm.infrastructure.persistence.jpa.UserProfileJpaRepository;
import com.gymcrm.infrastructure.persistence.mapper.UserDaoMapper;
import com.gymcrm.infrastructure.persistence.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class UserProfileRepositoryImplTest {

    @Mock
    UserProfileJpaRepository userProfileJpaRepository;

    @InjectMocks
    UserProfileRepositoryImpl repository;

    User domainUser;
    UserDao userDao;

    @BeforeEach
    void init() {
        domainUser = new User(1L, "john", "pass", "John", "Doe", true);
        userDao = UserDaoMapper.toDao(domainUser);
    }

    @Test
    void findProfileByUserName_shouldReturnDomainUser() {
        when(userProfileJpaRepository.findByUsername("john")).thenReturn(Optional.of(userDao));
        Optional<User> result = repository.findProfileByUsername("john");
        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
        verify(userProfileJpaRepository).findByUsername("john");
    }

    @Test
    void findProfileByUsername_shouldReturnEmptyIfNotFound() {
        when(userProfileJpaRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        Optional<User> result = repository.findProfileByUsername("unknown");
        assertFalse(result.isPresent());
    }

    @Test
    void updateProfile_shouldMergeDao() {
        repository.saveOrUpdate(domainUser);
        verify(userProfileJpaRepository).save(argThat((UserDao dao) ->
                dao.getId().equals(domainUser.getId()) &&
                        dao.getUsername().equals(domainUser.getUsername()) &&
                        dao.getFirstName().equals(domainUser.getFirstName())
        ));
    }

    @Test
    void existsByUserName_shouldReturnTrue() {
        when(userProfileJpaRepository.existsByUsername("john")).thenReturn(true);
        assertTrue(repository.existsByUserName("john"));
        verify(userProfileJpaRepository).existsByUsername("john");
    }

    @Test
    void existsByUserName_shouldReturnFalse() {
        when(userProfileJpaRepository.existsByUsername("unknown")).thenReturn(false);
        assertFalse(repository.existsByUserName("unknown"));
    }

    @Test
    public void testCountActiveUsers() {
        when(userProfileJpaRepository.countByIsActiveTrue()).thenReturn(10L);
        long result = repository.countActiveUsers();
        assertEquals(10L, result);
        verify(userProfileJpaRepository).countByIsActiveTrue();
    }
}
