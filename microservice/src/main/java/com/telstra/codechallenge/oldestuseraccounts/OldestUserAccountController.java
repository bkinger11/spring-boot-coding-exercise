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