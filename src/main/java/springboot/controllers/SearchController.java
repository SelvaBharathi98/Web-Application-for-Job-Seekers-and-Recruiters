package springboot.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springboot.models.AjaxResponseBody;
import springboot.models.Post;
import springboot.models.SearchCriteria;
import springboot.services.base.PostService;

import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    @Autowired
    PostService postService;

    @PostMapping("/search")
    public ResponseEntity<?> getSearchResultViaAjax(
            @Valid @RequestBody SearchCriteria search, Errors errors) {

        AjaxResponseBody result = new AjaxResponseBody();

        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }

        List<Post> posts = postService.findByTopic(search.getTopic());
        if (posts.isEmpty()) {
            result.setMsg("no post found!");
        } else {
            result.setMsg("success");
        }
        result.setResult(posts);

        return ResponseEntity.ok(result);

    }

}