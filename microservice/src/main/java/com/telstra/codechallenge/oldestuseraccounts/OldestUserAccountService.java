package com.telstra.codechallenge.oldestuseraccounts;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class OldestUserAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OldestUserAccountService.class);

    @Value("${oldestUserAccounts.base.url}")
    private String oldestUserAccountsBaseUrl;

    private final RestTemplate restTemplate;

    public OldestUserAccountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<OldestUserAccount> findOldestUserAccountsWithZeroFollowers(int count) {
        String url = oldestUserAccountsBaseUrl + "/search/users?q=followers:0&sort=joined&order=asc&per_page=" + count;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github.vnd.github.preview");

        try {
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<OldestUserAccountResponse> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    OldestUserAccountResponse.class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                OldestUserAccountResponse response = responseEntity.getBody();
                if (response != null && response.getItems() != null) {
                    return response.getItems().subList(0, Math.min(count, response.getItems().size()));
                }
            } else {
                LOGGER.error("Request failed with status code: {}", responseEntity.getStatusCode());
            }
        } catch (HttpClientErrorException ex) {
            LOGGER.error("Request failed with error: {} - {}", ex.getStatusCode(), ex.getStatusText());
        } catch (Exception ex) {
            LOGGER.error("Request failed with exception: {}", ex.getMessage());
        }

        return null;
    }
}
