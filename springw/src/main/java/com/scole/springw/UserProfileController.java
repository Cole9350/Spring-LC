package com.scole.springw;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class UserProfileController {

    private final GraphQLService graphQLService;

    public UserProfileController(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    @GetMapping("/profile/{username}")
    public Mono<Map<String, Map<String, UserProfile>>> getUserProfile(@PathVariable String username) {
        System.out.println(username);
        return graphQLService.fetchUserProfile(username);
    }
}
