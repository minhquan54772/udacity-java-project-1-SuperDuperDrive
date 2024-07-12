package com.udacity.jwdnd.course1.cloudstorage.controllers;


import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.udacity.jwdnd.course1.cloudstorage.services.HashService.encodeSalt;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;


    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @PostMapping("")
    public String createUpdateCredential(@ModelAttribute Credential credential, Authentication authentication, Model model) {
        String username = authentication.getName();
        int userid = this.userService.getUserByUsername(username).getUserid();

        int credentialId = credential.getCredentialid();
        if (credentialId >= 0) {
            // update credential
            Credential credentialById = this.credentialService.getCredentialById(credentialId);

            credentialById.setUrl(credential.getUrl());
            credentialById.setUsername(credential.getUsername());
            credentialById.setPassword(this.encryptionService.encryptValue(credential.getPassword(), credentialById.getKey()));

            this.credentialService.updateCredential(credentialById);
        } else {
            // create new credential
            credential.setUserid(userid);
            credential.setKey(encodeSalt());
            credential.setPassword(this.encryptionService.encryptValue(credential.getPassword(), credential.getKey()));

            this.credentialService.createCredential(credential);
        }
        model.addAttribute("success", true);
        model.addAttribute("errors", List.of());
        return "result";

    }

    @GetMapping("/{id}/delete")
    public String deleteCredential(@PathVariable int id, Authentication authentication, Model model) {
        this.credentialService.deleteCredential(id);
        model.addAttribute("success", true);
        model.addAttribute("errors", List.of());
        return "result";
    }
}
