package com.okta.springbootmongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@Controller
@RequestMapping(path = "/kayaks")
public class KayakController {

  @Autowired
  private KayakRepository kayakRepository;

  @PostMapping()
  public @ResponseBody
  String addNewUser(
      @RequestParam String name,
      @RequestParam String owner,
      @RequestParam Number value,
      @RequestParam String makeModel
  ) {
    Kayak kayak = new Kayak(name, owner, value, makeModel);
    kayakRepository.save(kayak);
    return "Saved";
  }

  @GetMapping()
  public @ResponseBody
  Flux<Kayak> getAllUsers() {
    Flux<Kayak> result = kayakRepository.findAll();
    return result;
  }

}