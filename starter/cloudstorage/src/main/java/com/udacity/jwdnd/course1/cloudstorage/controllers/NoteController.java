package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("")
    public String createUpdateNote(@ModelAttribute Note note, Authentication authentication, Model model) {
        String username = authentication.getName();
        int userid = this.userService.getUserByUsername(username).getUserid();

        int noteId = note.getNoteid();
        if (noteId >= 0) {
            // update note
            Note noteById = this.noteService.getNoteById(noteId, userid);
            if (noteById != null) {
                noteById.setNotetitle(note.getNotetitle());
                noteById.setNotedescription(note.getNotedescription());

                int i = this.noteService.updateNote(noteById, userid);
                model.addAttribute("success", true);
                model.addAttribute("errors", List.of());
                return "result";
            } else {
                model.addAttribute("success", false);
                model.addAttribute("errors", List.of("Note not found"));
                return "result";
            }

        } else {
            // create new note
            note.setUserid(userid);
            int i = this.noteService.addNote(note);
            model.addAttribute("success", true);
            model.addAttribute("errors", List.of());
            return "result";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteNote(@PathVariable int id, Authentication authentication, Model model) {
        String username = authentication.getName();
        int userid = this.userService.getUserByUsername(username).getUserid();

        Note noteById = this.noteService.getNoteById(id, userid);
        if (noteById != null) {
            this.noteService.deleteNote(id, userid);
            model.addAttribute("success", true);
            model.addAttribute("errors", List.of());
            return "result";
        } else {
            model.addAttribute("success", false);
            model.addAttribute("errors", List.of("Note not found"));
            return "result";
        }
    }
}
