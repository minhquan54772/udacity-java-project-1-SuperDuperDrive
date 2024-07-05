package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getNotesByUserId(int userId);

    @Insert("INSERT INTO NOTES (NOTETITLE, NOTEDESCRIPTION, USERID) VALUES (#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insertNote(Note note);

    @Update("UPDATE NOTES SET NODETITLE = #{notetitle}, NOTEDESCRIPTION = #{notedescription} WHERE NOTEID = #{noteid}")
    int updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE NOTEID = #{noteid}")
    void deleteNote(int noteid);
}
