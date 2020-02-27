package com.hhit.ciapp.controllers;

import com.hhit.ciapp.models.User;
import com.hhit.ciapp.services.SessionService;
import com.hhit.ciapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private UserService userService;

    private SessionService sessionService;

    @Autowired
    public LoginController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @RequestMapping(value = {"", "/", "login.html"}, method = RequestMethod.GET)
    public String login(HttpSession session, Model model) {

        //If already logged in load index.html
        if (sessionService.validateSession(session, model)) return "index";

        //Otherwise login.html
        return "login.html";

    }

    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
    public String loginControl(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model model) {

        //Login check
        if (userService.checkLogin(email, password)) {

            //Create an user object without password for session
            User sessionUser = userService.findUserByEmail(email);

            //Add user object to session without password
            sessionUser.setPassword("");
            session.setAttribute("user", sessionUser);

            return "redirect:/dashboard";
        } else {

            //Or show login page with an error
            model.addAttribute("message", "Username or password is incorrect");
            return "login.html";
        }

        }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    public String logout(HttpSession session, Model model) {
        if (session != null) {
            //Destroy the session variables
            session.invalidate();
        }
        //Add successful message to model.
        model.addAttribute("message", "Successfully Logged Out");
        return "redirect:/";  //Where you go after logout here.
    }

}