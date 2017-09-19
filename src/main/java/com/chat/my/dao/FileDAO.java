package com.chat.my.dao;

import com.chat.my.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Артем on 19.09.2017.
 */
public interface FileDAO extends JpaRepository<File, Long> {
}
