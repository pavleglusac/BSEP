package com.bsep.admin.myHouse;

import com.bsep.admin.exception.ForbiddenRealEstateAction;
import com.bsep.admin.model.Device;
import com.bsep.admin.model.User;
import com.bsep.admin.myHouse.dto.AddTenantDto;
import com.bsep.admin.myHouse.dto.DeviceDto;
import com.bsep.admin.myHouse.dto.RealEstateDto;
import com.bsep.admin.myHouse.dto.TenantDto;
import com.bsep.admin.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/myhouse")
public class MyHouseController {

    @Autowired
    MyHouseService houseService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/realestate/{email}")
    @PreAuthorize("hasAnyAuthority('READ_REAL_ESTATE', 'WRITE_REAL_ESTATE')")
    public ResponseEntity<List<RealEstateDto>> findRealEstatesForUser(@PathVariable @Email String email, Authentication authentication) {

        // read principal from token
        User user = (User) authentication.getPrincipal();
        if (user.hasAdminRole() || user.getEmail().equals(email)) {
            return ResponseEntity.ok(houseService.findRealEstatesForUser(email));
        }
        throw new ForbiddenRealEstateAction("Cannot view real estates for this user");
    }

    @PostMapping("/realestate/{email}")
    @PreAuthorize("hasAuthority('WRITE_REAL_ESTATE')")
    public ResponseEntity<RealEstateDto> addRealEstate(@PathVariable @Email String email, @Valid @RequestBody RealEstateDto realEstateDto) {
        return ResponseEntity.ok(houseService.addRealEstate(email, realEstateDto));
    }

    @PutMapping("/realestate/{email}")
    @PreAuthorize("hasAuthority('WRITE_REAL_ESTATE')")
    public ResponseEntity<RealEstateDto> editRealEstate(@PathVariable @Email String email, @Valid @RequestBody RealEstateDto realEstateDto) {
        return ResponseEntity.ok(houseService.editRealEstate(email, realEstateDto));
    }

    @PostMapping("/tenant")
    @PreAuthorize("hasAuthority('WRITE_REAL_ESTATE')")
    public ResponseEntity<TenantDto> addTenant(@Valid @RequestBody AddTenantDto addTenantDto) {
        return ResponseEntity.ok(houseService.addTenant(addTenantDto));
    }

    @PostMapping("/device")
    @PreAuthorize("hasAuthority('WRITE_REAL_ESTATE')")
    public ResponseEntity<Device> addDevice(@Valid @RequestBody DeviceDto device) {
        return ResponseEntity.ok(deviceService.addDevice(device));
    }

    @DeleteMapping("/device/{id}")
    @PreAuthorize("hasAuthority('WRITE_REAL_ESTATE')")
    public ResponseEntity<String> deleteDevice(@PathVariable String id) {
        deviceService.removeDevice(id);
        return ResponseEntity.ok("Device deleted");
    }
}
