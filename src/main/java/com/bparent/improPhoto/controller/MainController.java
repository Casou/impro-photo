package com.bparent.improPhoto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/preparation")
    public String preparation() {
        return "views/admin/preparation";
    }

    @RequestMapping("/impro/regie")
    public String regie(Model model) {
        model.addAttribute("side", "regie");
        return "views/impro/impro";
    }

    @RequestMapping("/impro/spectateur")
    public String spectateur(Model model) {
        model.addAttribute("side", "spectateur");
        return "views/impro/impro";
    }

}
