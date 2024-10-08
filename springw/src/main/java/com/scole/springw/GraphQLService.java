package com.scole.springw;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GraphQLService {

    private final WebClient webClient;
    private final UserProfileRepository userProfileRepository;

    public GraphQLService(WebClient webClient, UserProfileRepository userProfileRepository) {
        this.webClient = webClient;
        this.userProfileRepository = userProfileRepository;
    }

    public Mono<Map<String, Map<String, UserProfile>>> fetchUserProfile(String username) {
        return Mono.justOrEmpty(userProfileRepository.findById(username))
                .map(userProfile -> {
                    // Returning the cached profile as expected
                    return Map.of("data", Map.of("matchedUser", userProfile));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // Fetch from LeetCode if not found in cache
                    String graphqlQuery = """
                    query getUserProfile($username: String!) {
                      matchedUser(username: $username) {
                        username
                        submitStats: submitStatsGlobal {
                          acSubmissionNum {
                            difficulty
                            count
                          }
                        }
                      }
                    }
                """;

                    String requestBody = String.format("""
                    {
                        "query": "%s",
                        "variables": {
                            "username": "%s"
                        }
                    }
                """, graphqlQuery.replace("\n", "").replace("\"", "\\\""), username);

                    return webClient.post()
                            .uri("https://leetcode.com/graphql")
                            .header("Content-Type", "application/json")
                            .bodyValue(requestBody)
                            .retrieve()
                            .bodyToMono(Map.class)
                            .map(response -> {
                                // Parse the response and save it to MongoDB
                                Map<String, Object> data = (Map<String, Object>) response.get("data");
                                Map<String, Object> matchedUser = (Map<String, Object>) data.get("matchedUser");

                                UserProfile userProfile = new UserProfile();
                                userProfile.setUsername((String) matchedUser.get("username"));
                                List<Map<String, Object>> submitStats = (List<Map<String, Object>>) matchedUser.get("submitStats");
                                List<SubmissionStat> statsList = submitStats.stream()
                                        .map(stat -> new SubmissionStat((String) stat.get("difficulty"), (int) stat.get("count")))
                                        .toList();
                                userProfile.setSubmitStats(statsList);

                                // Save to MongoDB
                                userProfileRepository.save(userProfile);

                                // Return the nested map structure
                                return Map.of("data", Map.of("matchedUser", userProfile));
                            });
                }));
    }
}
