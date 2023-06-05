# Spring Boot Coding Exercise - Find the oldest user accounts with zero followers

This is a simple coding exercise that will allow you to demonstrate your knowledge
of spring boot by using a microservice to call a downstream service and return
some results.

## Olest User Account with Zero followers

### Oldest User Account 

```java
package com.telstra.codechallenge.oldestuseraccounts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OldestUserAccount {
    private long id;
    private String login;
    private String html_url;
}
```


### Oldest User Account Reponse

``` java
package com.telstra.codechallenge.oldestuseraccounts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OldestUserAccountResponse {
    private List<OldestUserAccount> items;
}
```
### Oldest User Account Service

```java
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

    /**
     * Returns an array of github oldest user accounts with zero followers.
     * Taken from <a href="https://api.github.com/search/users?q=followers:0&sort=joined&order=asc">https://api.github.com/search/users?q=followers:0&sort=joined&order=asc</a>.
     *
     * @return - oldest user account list.
     */
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

```

### Oldest User Account Controller

```java
package com.telstra.codechallenge.oldestuseraccounts;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OldestUserAccountController {
    private final OldestUserAccountService oldestUserAccountService;

    public OldestUserAccountController(OldestUserAccountService oldestUserAccountService) {
        this.oldestUserAccountService = oldestUserAccountService;
    }

    @GetMapping("/oldestAccounts")
    public List<OldestUserAccount> findOldestUserAccounts(@RequestParam(defaultValue = "10") int count) {
        return oldestUserAccountService.findOldestUserAccountsWithZeroFollowers(count);
    }
}
```


## Refactored existing code

### .getCatFacts() method 

```java
  public List<CatFact> getCatFacts() {
    CatFactResponse catFactResponse =
        restTemplate.getForObject(catFactsBaseUrl + "/facts", CatFactResponse.class);
     List<CatFact> catFacts = null;
     if (catFactResponse != null) {
       catFacts = catFactResponse.getData();
      }
    return catFacts;
  }
  ``` 
### Refactored .getCatFacts() method

```java
  public List<CatFact> getCatFacts() {
    CatFactResponse catFactResponse =
        restTemplate.getForObject(catFactsBaseUrl + "/facts", CatFactResponse.class);
    return catFactResponse != null ? catFactResponse.getData() : null;
  }
  ```
