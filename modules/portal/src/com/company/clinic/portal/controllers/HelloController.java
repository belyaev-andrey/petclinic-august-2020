package com.company.clinic.portal.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping(value = "hello/{name}", produces = "text/plain")
    public String sayHello(@PathVariable String name) {
        return "Hello, "+name;
    }

}
