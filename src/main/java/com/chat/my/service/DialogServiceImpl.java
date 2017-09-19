package com.chat.my.service;

import com.chat.my.dao.DialogDAO;
import com.chat.my.model.DialogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Артем on 17.09.2017.
 */
@Service
public class DialogServiceImpl implements DialogService {

    @Autowired
    private  DialogDAO dialogDAO;

    @Override
    public void save(DialogEntity dialogEntity) {
        dialogDAO.save(dialogEntity);
    }

    @Override
    public DialogEntity findByUser1User2(String user1, String user2) {
        return dialogDAO.findByUser1AndUser2(user1,user2);

    }
}
