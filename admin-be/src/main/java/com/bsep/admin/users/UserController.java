package com.bsep.admin.users;

import com.bsep.admin.users.dto.UserDisplayDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserDisplayDto>> search(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam int page,
            @RequestParam int amount,
            @RequestParam(required = false, defaultValue = "ROLE_ADMIN,ROLE_TENANT,ROLE_LANDLORD") List<String> roles,
            @RequestParam(required = false, defaultValue = "false") boolean onlyLocked
            ) {
        return ResponseEntity.ok(userService.search(query, page, amount, roles, onlyLocked));
    }

}
