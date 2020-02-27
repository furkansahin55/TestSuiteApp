package com.hhit.ciapp.services;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

@Service()
public class SessionService {

    public boolean validateSession (HttpSession session, Model model ){
        //If a session variable named user is not initialized means user not logged in yet.
        //Add error message attribute to model and return false
        if (session.getAttribute("user") == null){
            model.addAttribute("message","Please Login");
            return false;
        } else {
            return true;
        }
    }
}
