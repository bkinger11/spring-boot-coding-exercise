package com.telstra.codechallenge.oldestuseraccounts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OldestUserAccountResponse {
    private List<OldestUserAccount> items;
}
