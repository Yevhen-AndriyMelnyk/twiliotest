package com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/language")
public class LanguageController {


    @GetMapping
    public String getLanguage(){
        final String[] languages = {"Ukrainian", "English"};
        return languages[new Random().nextInt(languages.length)];
    }


}
