package com.bparent.improPhoto.controller;

import com.bparent.improPhoto.dto.BasicCodeLabelDto;
import com.bparent.improPhoto.enums.CategorieTypeEnum;
import com.bparent.improPhoto.util.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
    public String regie(Model model) throws IOException {
        model.addAttribute("side", "regie");
        return "views/impro/index";
    }

    @RequestMapping("/impro/spectateur")
    public String spectateur(Model model) throws IOException {
        model.addAttribute("side", "spectateur");
        return "views/impro/index";
    }

}
