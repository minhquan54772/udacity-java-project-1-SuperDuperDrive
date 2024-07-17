package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE USERID = #{userId}")
    List<Credential> getCredentialsByUserId(int userId);

    @Select("SELECT * FROM CREDENTIALS WHERE CREDENTIALID = #{credentialId} AND USERID = #{userId}")
    Credential getCredentialsById(int credentialId, int userId);

    @Insert("INSERT INTO CREDENTIALS (URL, USERNAME, KEY, PASSWORD, USERID) VALUES (#{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int createCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET URL = #{url}, USERNAME = #{username}, PASSWORD = #{password} WHERE CREDENTIALID = #{credentialid} AND USERID = #{userId}")
    int updateCredential(Credential credential, int userId);

    @Delete("DELETE FROM CREDENTIALS WHERE CREDENTIALID = #{credentialId} AND USERID = #{userId}")
    void deleteCredential(int credentialId, int userId);

}
