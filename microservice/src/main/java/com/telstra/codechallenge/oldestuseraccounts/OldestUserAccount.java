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
