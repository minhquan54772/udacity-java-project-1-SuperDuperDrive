package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE USERID = #{userId}")
    List<Credential> getCredentialsByUserId(int userId);

    @Insert("INSERT INTO CREDENTIALS (URL, USERNAME, KEY, PASSWORD, USERID) VALUES (#{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int createCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET URL = #{url}, USERNAME = #{username}, USERNAME = #{username}, USERNAME = #{username} WHERE CREDENTIALID = #{noteid}")
    int updateCredential(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE CREDENTIALID = #{credentialId}")
    void deleteCredential(int credentialId);

}
