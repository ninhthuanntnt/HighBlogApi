package com.high.highblog.repository;

import com.high.highblog.model.entity.PostStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostStatisticRepository extends JpaRepository<PostStatistic, Long> {

    List<PostStatistic> findByPostIdIn(Collection<Long> postIds);
}
