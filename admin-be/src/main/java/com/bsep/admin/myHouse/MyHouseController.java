package com.bsep.admin.myHouse;

import com.bsep.admin.exception.ForbiddenRealEstateAction;
import com.bsep.admin.model.User;
import com.bsep.admin.myHouse.dto.AddTenantDto;
import com.bsep.admin.myHouse.dto.RealEstateDto;
import com.bsep.admin.myHouse.dto.TenantDto;
import com.bsep.admin.repository.UserRepository;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/myhouse")
public class MyHouseController {

    @Autowired
    MyHouseService houseService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/realestate/{email}")
    @PreAuthorize("hasAnyAuthority('READ_REAL_ESTATE', 'WRITE_REAL_ESTATE')")
    public ResponseEntity<List<RealEstateDto>> findRealEstatesForUser(@PathVariable String email, Authentication authentication) {

        // read principal from token
        User user = (User) authentication.getPrincipal();
        if (user.hasAdminRole() || user.getEmail().equals(email)) {
            return ResponseEntity.ok(houseService.findRealEstatesForUser(email));
        }
        throw new ForbiddenRealEstateAction("Cannot view real estates for this user");
    }

    @PostMapping("/realestate/{email}")
    @PreAuthorize("hasAuthority('WRITE_REAL_ESTATE')")
    public ResponseEntity<RealEstateDto> addRealEstate(@PathVariable String email, @RequestBody RealEstateDto realEstateDto) {
        return ResponseEntity.ok(houseService.addRealEstate(email, realEstateDto));
    }

    @PutMapping("/realestate/{email}")
    @PreAuthorize("hasAuthority('WRITE_REAL_ESTATE')")
    public ResponseEntity<RealEstateDto> editRealEstate(@PathVariable String email, @RequestBody RealEstateDto realEstateDto) {
        return ResponseEntity.ok(houseService.editRealEstate(email, realEstateDto));
    }

    @PostMapping("/tenant")
    @PreAuthorize("hasAuthority('WRITE_REAL_ESTATE')")
    public ResponseEntity<TenantDto> addTenant(@RequestBody AddTenantDto addTenantDto) {
        return ResponseEntity.ok(houseService.addTenant(addTenantDto));
    }
}
