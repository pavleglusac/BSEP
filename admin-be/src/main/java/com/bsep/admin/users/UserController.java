package com.bsep.admin.users;

import com.bsep.admin.users.dto.RoleChangeDto;
import com.bsep.admin.users.dto.UserDisplayDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('SEARCH_USER')")
    public ResponseEntity<Page<UserDisplayDto>> search(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam @Min(0) int page,
            @RequestParam @Min(1) @Max(20) int amount,
            @RequestParam(required = false, defaultValue = "ROLE_ADMIN,ROLE_TENANT,ROLE_LANDLORD") List<String> roles,
            @RequestParam(required = false, defaultValue = "false") boolean onlyLocked
            ) {
        return ResponseEntity.ok(userService.search(query, page, amount, roles, onlyLocked));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_USER')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok(Map.of("message", "User successfully deleted."));
    }

    @PatchMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('WRITE_USER')")
    public ResponseEntity<Map<String, String>> changeRole(@PathVariable UUID id,@Valid @RequestBody RoleChangeDto dto) {
        userService.changeRole(id, dto);
        return ResponseEntity.ok(Map.of("message", "Role successfully changed."));
    }

}
