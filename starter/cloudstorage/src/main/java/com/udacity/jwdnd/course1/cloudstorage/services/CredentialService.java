package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;


    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public List<Credential> getCredentialsByUserId(int userId) {
        return this.credentialMapper.getCredentialsByUserId(userId);
    }

    public int createCredential(Credential credential) {
        return this.credentialMapper.createCredential(credential);
    }

    public int updateCredential(Credential credential) {
        return this.credentialMapper.updateCredential(credential);
    }

    public void deleteCredential(int credentialId) {
        this.credentialMapper.deleteCredential(credentialId);
    }

}
