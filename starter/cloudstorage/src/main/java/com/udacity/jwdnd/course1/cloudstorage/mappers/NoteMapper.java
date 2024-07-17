package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getNotesByUserId(int userId);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId} AND USERID = #{userId}")
    Note getNoteByNoteId(int noteId, int userId);

    @Insert("INSERT INTO NOTES (NOTETITLE, NOTEDESCRIPTION, USERID) VALUES (#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insertNote(Note note);

    @Update("UPDATE NOTES SET NOTETITLE = #{notetitle}, NOTEDESCRIPTION = #{notedescription} WHERE NOTEID = #{noteid} AND USERID = #{userId}")
    int updateNote(Note note, int userId);

    @Delete("DELETE FROM NOTES WHERE NOTEID = #{noteid} AND USERID = #{userId}")
    void deleteNote(int noteid, int userId);
}
