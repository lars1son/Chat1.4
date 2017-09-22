package com.chat.my.controller;


import com.chat.beans.Dialog;
import com.chat.exceptions.PhotoErrorException;
import com.chat.my.dao.DialogDAO;
import com.chat.my.dao.UserDao;
import com.chat.my.generics.DialogsMap;
import com.chat.my.generics.OnlineUsersMap;
import com.chat.my.model.DialogEntity;
import com.chat.my.model.Image;
import com.chat.my.model.User;
import com.chat.my.model.UserEntity;
import com.chat.my.service.DialogService;
import com.chat.my.service.SecurityService;
import com.chat.my.service.UserService;
import com.chat.my.validator.UserValidator;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;

@CrossOrigin
@Controller
public class UserController {
    private static final String relative_path="C:\\Users\\Артем.Артем-ПК\\Documents\\Учебники\\Java\\MyChat1.3\\src\\main\\webapp\\resources\\images\\loaded\\";
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private DialogService dialogService;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {

        model.addAttribute("userForm", new UserEntity());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") UserEntity userForm, BindingResult bindingResult, Model model, HttpSession session) {

        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userForm.setAbout("I'm so shy to tell something :)");
        userForm.setEmail("urEmail@mail.ru");
        userService.save(userForm,false);
        session.setAttribute("user", userForm);
        securityService.autoLogin(userForm.getUsername(), userForm.getConfirmPassword());
        model.addAttribute("username", userForm.getUsername());
        return "redirect:/chat";
    }


    @RequestMapping(value = "/dialog", method = RequestMethod.GET)
    public String dialog(Model model, HttpServletRequest request) {
        if ((request.getParameter("user1") != null) && (request.getParameter("user2") != null)) {
            if (!DialogsMap.dialogsUserMap.containsKey(request.getParameter("user1") + request.getParameter("user2"))) {
                DialogsMap.dialogsUserMap.put(request.getParameter("user1") + request.getParameter("user2"), new Dialog(new User(OnlineUsersMap.onlineUserMap.get(request.getParameter("user1"))), new User(OnlineUsersMap.onlineUserMap.get(request.getParameter("user2")))));
            }
            DialogEntity dialogEntity = dialogService.findByUser1User2(request.getParameter("user1"), request.getParameter("user2"));
            if (dialogEntity == null) {
                dialogEntity = new DialogEntity(request.getParameter("user1"), request.getParameter("user2"));
                dialogService.save(dialogEntity);
            }

            model.addAttribute("user1", request.getParameter("user1"));
            model.addAttribute("user2", request.getParameter("user2"));
            model.addAttribute("whos", request.getParameter("whos"));
            model.addAttribute("image_path", "/resources/images/rasta_back.jpg");

        }
        return "dialog";
    }
    //////////save changes in my profile
    @RequestMapping(value = "/save_changes", method = RequestMethod.POST)
    public String onAddPhoto(@RequestParam String password, @RequestParam String email, @RequestParam String about, @RequestParam(name = "file", required = false) MultipartFile multipartFile) {
        Boolean acces = false;
        UserEntity user = getUser();
        if (multipartFile != null && !multipartFile.isEmpty() && multipartFile.getOriginalFilename()!="") {
            String newfilename = user.getId().toString() + multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().length() - 4, multipartFile.getOriginalFilename().length());//Поставить на ид польхователя.
            String path = relative_path + newfilename;
            try {
                File file = new File(path);
                FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            path = ("/resources/images/loaded/" + newfilename).toLowerCase();

            Image newIm = new Image(path);
            if (user.getImage() == null) {
                user.setImage(newIm);
            } else {
                user.getImage().setPath(newIm.getPath());
            }
            acces=true;


        }
        if (password != null && password.length() >= 8 && password!=user.getPassword()) {
            user.setPassword(password);
            acces=true;
        }
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
            acces=true;
        }
        if (about != null && !about.isEmpty()) {
            user.setAbout(about);
            acces=true;
        }
        if (acces){
        userService.save(user, true);}
        return "chat";
    }
///////
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Username or password is incorrect.");
        }

        if (logout != null) {
            model.addAttribute("message", "Logged out successfully.");

        }

        return "login";
    }
/////// chat controller
    @RequestMapping(value = {"/", "/chat"}, method = RequestMethod.GET)
    public String chat(Model model, HttpSession session) {
        UserEntity u = getUser();
        User user = new User();
        user.createUserFromUserEntity(u);
        OnlineUsersMap.onlineUserMap.put(u.getUsername(), user);
        session.setAttribute("user", u);
        model.addAttribute("username", u.getUsername());
        return "chat";
    }

///////profile controller
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model model, HttpServletRequest request) {
        UserEntity u = getUser();
        model.addAttribute("email", u.getEmail());
        model.addAttribute("about", u.getAbout());
        if (u.getImage() != null) {
            model.addAttribute("image_path", u.getImage().getPath());
        } else model.addAttribute("image_path", "resources/images/loaded/standart.jpg");
        return "profile";
    }
//////// Doesn't exist
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model) {
        return "admin";
    }


    /// Get UserEntity from authenticaion
    public UserEntity getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object obj = auth.getPrincipal();
        String username = "";
        if (obj instanceof UserDetails) {
            username = ((UserDetails) obj).getUsername();
        } else {
            username = obj.toString();
        }
        UserEntity u = userService.findByUsername(username);
        return u;
    }
}

