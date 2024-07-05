package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE USERID = #{userId}")
    List<File> getFilesListByUserId(int userId);

    @Insert("INSERT INTO FILES (FILENAME, CONTENTTYPE, FILESIZE, USERID FILEDATA) VALUES (#{filename}, #{contenttype} #{filesize} #{userid} #{filedata})")
    int uploadFile(File file);

    @Delete("DELETE FROM FILES WHERE FILEID = #{fileId}")
    void deleteFile(int fileId);
}
