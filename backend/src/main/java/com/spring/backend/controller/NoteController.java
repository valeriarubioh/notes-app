package com.spring.backend.controller;

import com.spring.backend.payload.request.CategoryRequest;
import com.spring.backend.payload.request.NoteRequest;
import com.spring.backend.payload.response.NoteResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.spring.backend.service.NoteService;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<NoteResponse>> getNotes(@RequestParam(required = false) Boolean archived) {

        if (!Objects.isNull(archived)) {
            return new ResponseEntity<>(noteService.getNotesByFilter(archived), HttpStatus.OK);
        }
        return new ResponseEntity<>(noteService.getAllNotes(), HttpStatus.OK);

    }

    @GetMapping("/{idNote}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<NoteResponse> getNotes(@PathVariable Long idNote) {
        return new ResponseEntity<>(noteService.getNoteById(idNote), HttpStatus.OK);

    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<NoteResponse> saveNote(@RequestBody @Valid NoteRequest noteRequest) {
        return new ResponseEntity<>(noteService.saveNote(noteRequest), HttpStatus.CREATED);
    }

    @PostMapping("/{idNote}/addCategories")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<NoteResponse> addCategoriesToNote(@RequestBody @Valid Set<CategoryRequest> categoryRequests,
                                                            @PathVariable Long idNote) {
        return new ResponseEntity<>(noteService.addCategoriesToNote(categoryRequests, idNote), HttpStatus.OK);
    }

    @PostMapping("/{idNote}/removeCategories")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<NoteResponse> removeCategoriesToNote(@RequestBody @Valid Set<CategoryRequest> categoryRequests,
                                                            @PathVariable Long idNote) {
        return new ResponseEntity<>(noteService.removeCategoriesToNote(categoryRequests, idNote), HttpStatus.OK);
    }
    @DeleteMapping({"/{idNote}"})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNote(@PathVariable Long idNote) {
        noteService.deleteNote(idNote);
        return ResponseEntity.noContent().build();
    }

    @PutMapping({"/{idNote}"})
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable Long idNote, @RequestBody @Valid NoteRequest noteRequest) {
        return new ResponseEntity<>(noteService.updateNote(idNote,noteRequest), HttpStatus.OK);
    }
}
