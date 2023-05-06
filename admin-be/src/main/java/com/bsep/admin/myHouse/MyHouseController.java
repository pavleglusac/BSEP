package com.bsep.admin.myHouse;

import com.bsep.admin.myHouse.dto.AddTenantDto;
import com.bsep.admin.myHouse.dto.RealEstateDto;
import com.bsep.admin.myHouse.dto.TenantDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/myhouse")
public class MyHouseController {

    @Autowired
    MyHouseService houseService;

    @GetMapping("/realestate/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<RealEstateDto>> findRealEstatesForUser(@PathVariable String email) {
        return ResponseEntity.ok(houseService.findRealEstatesForUser(email));
    }

    @PostMapping("/realestate/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RealEstateDto> addRealEstate(@PathVariable String email, @RequestBody RealEstateDto realEstateDto) {
        return ResponseEntity.ok(houseService.addRealEstate(email, realEstateDto));
    }

    @PutMapping("/realestate/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RealEstateDto> editRealEstate(@PathVariable String email, @RequestBody RealEstateDto realEstateDto) {
        return ResponseEntity.ok(houseService.editRealEstate(email, realEstateDto));
    }

    @PostMapping("/tenant")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TenantDto> addTenant(@RequestBody AddTenantDto addTenantDto) {
        return ResponseEntity.ok(houseService.addTenant(addTenantDto));
    }
}
