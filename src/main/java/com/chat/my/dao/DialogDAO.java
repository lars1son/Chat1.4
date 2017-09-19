package com.chat.my.dao;

import com.chat.my.model.DialogEntity;
import com.chat.my.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Артем on 17.09.2017.
 */
public interface DialogDAO extends JpaRepository <DialogEntity, Long>{
    @Query("SELECT d FROM DialogEntity d where (d.user1 = :user1 or d.user1=:user2) AND (d.user2 = :user2 or d.user2=:user1)")
    DialogEntity findByUser1AndUser2(@Param("user1") String user1, @Param("user2") String user2);

}