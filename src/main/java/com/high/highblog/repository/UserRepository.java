package com.high.highblog.repository;

import com.high.highblog.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIdIn(Collection<Long> ids);

    Optional<User> findByNickName(String nickName);

    boolean existsByNickName(String nickName);

    @Query("SELECT u FROM User u"
            + " JOIN Account acc ON acc.userId = u.id "
            + "                  AND acc.id = :accountId")
    Optional<User> getByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT DISTINCT u FROM  User u"
            + " WHERE (:nickname IS NULL OR u.nickName = :nickname) ")
    Page<User> fetchUsers(String nickname, Pageable pageable);
}
