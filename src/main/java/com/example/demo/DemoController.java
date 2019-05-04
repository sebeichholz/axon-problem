package com.example.demo;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class DemoController {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private EventHelper eventHelper;


    @GetMapping
    public String home(Model model) {
        return "home";
    }


//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }

    @GetMapping("/user")
    public String user(Model model) {
        model.addAttribute("events", eventHelper.getEventInformation("id1"));
        return "user";
    }

    @GetMapping(value = "/logout")
    public RedirectView logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return new RedirectView("/");
    }


    @PostMapping("/user")
    public RedirectView postUser(Model model
            , @RequestParam (required = false, defaultValue = "") String action
    ) {

        switch (action) {
            case "create":
                CreateDemoAggregateCommand command = new CreateDemoAggregateCommand(
                        //UUID.randomUUID().toString()
                        "id1"
                        , "new aggregate");
                commandGateway.send(command);
                break;
        }

        RedirectView rv = new RedirectView();
        rv.setUrl("/user");
        return rv;

    }



}
