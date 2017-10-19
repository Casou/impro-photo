package com.bparent.improPhoto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    @RequestMapping("/preparation")
    public String preparation() {
        return "views/admin/preparation.html";
    }

}
