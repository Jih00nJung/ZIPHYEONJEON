package io.pjj.ziphyeonjeon.interaction.repository;

import io.pjj.ziphyeonjeon.interaction.entity.Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordsRepository extends JpaRepository<Records, Long> {
    Optional<Records> findByUserIdAndHouseId(Long userId, Long houseId);

    List<Records> findByUserIdOrderByViewedTimeDesc(Long userId);
}
