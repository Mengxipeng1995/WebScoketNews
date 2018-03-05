package com.mxp.dao;

import com.mxp.entiy.User;
import org.apache.ibatis.annotations.Param;


public interface UserMapper {
    User findByUserName(@Param("name") String username);
}
