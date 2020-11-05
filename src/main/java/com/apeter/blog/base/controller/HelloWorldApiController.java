package com.apeter.blog.base.controller;

import com.apeter.blog.base.api.request.PostRequest;
import com.apeter.blog.base.api.response.IndexResponse;
import com.apeter.blog.base.routers.ExampleRoutes;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldApiController {

    @GetMapping(value = "/")
    public IndexResponse index(){
        return IndexResponse.builder()
                .message("Hello World!")
                .build();
    }

    @GetMapping(ExampleRoutes.GET)
    public IndexResponse getExample(){
        return IndexResponse.builder()
                .message("ExampleRoutes.Get response")
                .build();
    }

    @GetMapping(ExampleRoutes.GET_WITH_PARAMETERS)
    public IndexResponse getExampleWithParams(
            @RequestParam String param1,
            @RequestParam(value = "par_am2") String param2,
            @RequestParam(required = false, defaultValue = "wow") String param3,
            @RequestParam(required = false) String param4
    ){
        return IndexResponse.builder()
                .message(
                        "get with param: " + param1
                                + " and " + param2
                                + " and " + param3
                                + " and " + param4
                ).build();
    }

    @GetMapping(ExampleRoutes.GET_WITH_PATH)
    public IndexResponse getExampleWithPath(
            @PathVariable String pathVariable
    ){
        return IndexResponse.builder()
                .message(pathVariable).build();
    }

    @PostMapping(ExampleRoutes.POST)
    public IndexResponse post(
            @RequestBody PostRequest request
            ){
        return IndexResponse.builder()
                .message(request.toString())
                .build();
    }

    @PutMapping(ExampleRoutes.PUT)
    public IndexResponse put(
            @PathVariable String id,
            @RequestBody PostRequest request
    ){
        return IndexResponse.builder()
                .message(id + " " + request)
                .build();
    }

    @DeleteMapping(ExampleRoutes.DELETE)
    public IndexResponse delete(@PathVariable String id){
        return IndexResponse.builder()
                .message(id)
                .build();
    }

}
