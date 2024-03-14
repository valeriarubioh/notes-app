package com.spring.backend.service;

import com.spring.backend.entity.CategoryEntity;
import com.spring.backend.entity.NoteEntity;
import com.spring.backend.entity.UserEntity;
import com.spring.backend.exception.BusinessException;
import com.spring.backend.payload.request.CategoryRequest;
import com.spring.backend.payload.request.NoteRequest;
import com.spring.backend.payload.response.CategoryResponse;
import com.spring.backend.payload.response.NoteResponse;
import com.spring.backend.repository.CategoryRepository;
import com.spring.backend.repository.NoteRepository;
import com.spring.backend.repository.UserRepository;
import com.spring.backend.utilities.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NoteService {
    private final NoteRepository noteRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, CategoryService categoryService, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public NoteResponse saveNote(NoteRequest noteRequest) {
        NoteEntity noteEntity = noteRequestToNoteEntity(noteRequest);
        if (!Objects.isNull(noteRequest.getCategory())) {
            CategoryEntity categoryEntity = categoryRepository.findByCategoryName(noteRequest.getCategory().getCategoryName())
                    .orElseGet(() -> {
                        CategoryEntity entity = categoryService.categoryRequestToCategoryEntity(noteRequest.getCategory());
                        return categoryRepository.save(entity);
                    });
            HashSet<CategoryEntity> categoryEntities = new HashSet<>();
            categoryEntities.add(categoryEntity);
            noteEntity.setCategories(categoryEntities);
        }

        NoteEntity noteEntitySaved = noteRepository.save(noteEntity);
        return noteEntityToNoteResponse(noteEntitySaved);
    }

    public NoteResponse addCategoriesToNote(Set<CategoryRequest> categoryRequests, Long idNote) {
        NoteEntity noteEntity = noteRepository.findById(idNote)
                .orElseThrow(() -> new BusinessException("Note does not exist"));
        final Set<CategoryEntity> categoryEntities = categoryRequests.stream()
                .map(categoryService::categoryRequestToCategoryEntity).collect(Collectors.toSet());
        HashSet<CategoryEntity> categoryEntitiesSaved = new HashSet<>();
        for (CategoryEntity categoryEntity : categoryEntities) {
            CategoryEntity categoryEntitySaved = categoryRepository.findByCategoryName(categoryEntity.getCategoryName())
                    .orElseGet(() -> categoryRepository.save(categoryEntity));
            categoryEntitiesSaved.add(categoryEntitySaved);
        }
        noteEntity.setCategories(categoryEntitiesSaved);
        return noteEntityToNoteResponse(noteRepository.save(noteEntity));
    }



    public NoteResponse removeCategoriesToNote(Set<CategoryRequest> categoryRequests, Long idNote) {
        NoteEntity noteEntity = noteRepository.findById(idNote)
                .orElseThrow(() -> new BusinessException("Note does not exist"));
        Set<CategoryEntity> noteCategories = noteEntity.getCategories();
        for (CategoryRequest categoryRequest : categoryRequests) {
            Optional<CategoryEntity> optionalCategoryEntity = noteCategories.stream()
                    .filter(categoryEntity ->
                            Objects.equals(categoryEntity.getCategoryName(), categoryRequest.getCategoryName()))
                    .findFirst();
            if (optionalCategoryEntity.isPresent()) {
                CategoryEntity categoryEntity = optionalCategoryEntity.get();
                noteCategories.remove(categoryEntity);
            }
        }
        return noteEntityToNoteResponse(noteRepository.save(noteEntity));
    }

    private NoteEntity noteRequestToNoteEntity(NoteRequest noteRequest) {
        return NoteEntity
                .builder()
                .title(noteRequest.getTitle())
                .content(noteRequest.getContent())
                .archived(noteRequest.getArchived())
                .userEntity(userRepository.findByUsername(Util.getUserAuthenticated().getUsername())
                        .orElseThrow(RuntimeException::new))
                .build();
    }

    private NoteResponse noteEntityToNoteResponse(NoteEntity noteEntity) {
        Set<CategoryEntity> categories = noteEntity.getCategories();
        Set<CategoryResponse> categoryResponses = new HashSet<>();
        if (!Objects.isNull(categories)) {
            categories.stream()
                    .map((categoryService::categoryEntityToCategoryResponse)).collect(Collectors.toSet());
        }
        return NoteResponse
                .builder()
                .id(noteEntity.getId())
                .title(noteEntity.getTitle())
                .content(noteEntity.getContent())
                .archived(noteEntity.getArchived())
                .username(noteEntity.getUserEntity().getUsername())
                .categories(categoryResponses)
                .createdAt(noteEntity.getCreatedAt())
                .updatedAt(noteEntity.getUpdatedAt())
                .build();
    }

    public List<NoteResponse> getAllNotes() {
        List<NoteEntity> noteEntities = noteRepository.findAllByUserEntityId(Util.getUserAuthenticated().getId());
        return noteEntities.stream().map(this::noteEntityToNoteResponse).toList();
    }

    public NoteResponse getNoteById(Long idNote) {
        NoteEntity noteEntity = noteRepository.findByIdAndUserEntityId(idNote, Util.getUserAuthenticated().getId())
                .orElseThrow(() -> new BusinessException("Id note not found"));
        return noteEntityToNoteResponse(noteEntity);
    }

    public List<NoteResponse> getNotesByFilter(Boolean archived) {
        List<NoteEntity> noteEntities = noteRepository.findAllByUserEntityIdAndArchived(Util.getUserAuthenticated().getId(), archived);
        return noteEntities.stream().map(this::noteEntityToNoteResponse).toList();
    }

    public void deleteNote(Long idNote) {
        NoteEntity noteEntity = noteRepository.findById(idNote)
                .orElseThrow(RuntimeException::new);
        Optional<UserEntity> userAuthenticated = userRepository.findByUsername(Util.getUserAuthenticated().getUsername());

        if (userAuthenticated.isPresent() && !Objects.equals(noteEntity.getUserEntity().getUsername(), userAuthenticated.get().getUsername())) {
            throw new BusinessException("Accion no permitida");
        }
        noteRepository.delete(noteEntity);
    }

    public NoteResponse updateNote(Long idNote, NoteRequest noteRequest) {
        NoteEntity noteEntity = noteRepository.findById(idNote)
                .orElseThrow(RuntimeException::new);
        Optional<UserEntity> userAuthenticated = userRepository.findByUsername(Util.getUserAuthenticated().getUsername());
        if (userAuthenticated.isPresent() && !Objects.equals(noteEntity.getUserEntity().getUsername(), userAuthenticated.get().getUsername())) {
            throw new BusinessException("Accion no permitida");
        }
        if (!Objects.isNull(noteRequest.getTitle()) && !Objects.equals(noteEntity.getTitle(), noteRequest.getTitle())) {
            noteEntity.setTitle(noteRequest.getTitle());
        }
        if (!Objects.isNull(noteRequest.getContent()) && !Objects.equals(noteEntity.getContent(), noteRequest.getContent())) {
            noteEntity.setContent(noteRequest.getContent());
        }
        if (!Objects.isNull(noteRequest.getArchived()) && !Objects.equals(noteEntity.getArchived(), noteRequest.getArchived())) {
            noteEntity.setArchived(noteRequest.getArchived());
        }
        NoteEntity noteEntitySaved = noteRepository.save(noteEntity);
        return noteEntityToNoteResponse(noteEntitySaved);
    }
}
