package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.enums.CategorieTypeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/preparation")
    public String preparation(Model model) {
        model.addAttribute("categorieTypes", Arrays.stream(CategorieTypeEnum.values())
                .map(type -> new BasicCodeLabelDto(type.getCode(), type.getLabel()))
                .collect(Collectors.toList()));
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
