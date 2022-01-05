package org.wcs.batchjobworkshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {
    @ResponseBody
    @GetMapping("")
    Map<String, String> index() {
        Map<String, String> msg = new HashMap<>();
        msg.put("message", "Welcome to batch job workshop");

        return msg;
    }
}
