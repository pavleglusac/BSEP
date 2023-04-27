package com.bsep.admin.users;

import com.bsep.admin.users.dto.UserDisplayDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok(Map.of("message", "User successfully deleted."));
    }

}
