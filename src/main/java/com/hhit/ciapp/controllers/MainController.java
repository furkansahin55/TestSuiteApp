package com.hhit.ciapp.controllers;

import com.hhit.ciapp.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    private SessionService sessionService;

    @Autowired
    public MainController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping(value = {"/dashboard"}, method = RequestMethod.GET)
    public String login(HttpSession session, Model model) {
        //If session not valid redirect to login
        if (!sessionService.validateSession(session, model)) return "login";

        return "index";
    }


}