package com.spring.backend.repository;

import com.spring.backend.entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

    List<NoteEntity> findAllByUserEntityId(Long id);
    Optional<NoteEntity> findByIdAndUserEntityId(Long id,Long userEntityId);

    List<NoteEntity> findAllByUserEntityIdAndArchived(Long id, Boolean archived);
}
