package com.chat.my.generics;


import com.chat.my.model.User;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OnlineUsersMap {
    public static Map<String, User> onlineUserMap = new ConcurrentHashMap();

    public static String getOnlineUserLoginsThroughTheSpaceLastSpaceIncluding(){
        StringBuilder result = new StringBuilder();
        Set<String> logins = onlineUserMap.keySet();
        for (String i : logins){
                result.append(i);
                result.append(" ");
        }
        return result.toString();
    }


}
