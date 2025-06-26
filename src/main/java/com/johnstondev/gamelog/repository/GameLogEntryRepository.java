package com.johnstondev.gamelog.repository;

import com.johnstondev.gamelog.model.GameLogEntry;
import com.johnstondev.gamelog.model.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameLogEntryRepository extends JpaRepository<GameLogEntry, Long> {

    // find all entries for a specific user by their id
    List<GameLogEntry> findByUserId(Long userId);

    // find entries for a user with specific status (for filtering)
    List<GameLogEntry> findByUserIdAndStatus(Long userId, GameStatus status);

    // find a specific entry by user and entry ID (for updates/deletes)
    Optional<GameLogEntry> findByIdAndUserId(Long entryId, Long userId);
}