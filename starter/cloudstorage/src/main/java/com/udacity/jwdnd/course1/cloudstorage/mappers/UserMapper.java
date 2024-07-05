package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE USERNAME = #{username}")
    User getUserByUsername(String username);

    @Insert("INSERT INTO USERS (USERNAME, SALT, PASSWORD, FIRSTNAME, LASTNAME) VALUES ( #{username}, #{salt}, #{password}, #{firstname}, #{lastname} )")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insertUser(User user);

}
