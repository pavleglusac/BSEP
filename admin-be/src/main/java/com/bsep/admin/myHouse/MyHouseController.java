package com.bsep.admin.myHouse;

import com.bsep.admin.exception.DeviceNotFoundException;
import com.bsep.admin.exception.ForbiddenDeviceAction;
import com.bsep.admin.exception.ForbiddenRealEstateAction;
import com.bsep.admin.model.Device;
import com.bsep.admin.model.Message;
import com.bsep.admin.model.RealEstate;
import com.bsep.admin.model.User;
import com.bsep.admin.myHouse.dto.*;
import com.bsep.admin.repository.LandlordRepository;
import com.bsep.admin.repository.RealEstateRepository;
import com.bsep.admin.repository.TenantRepository;
import com.bsep.admin.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    LandlordRepository  landlordRepository;

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

    @GetMapping("/device/{id}/messages")
    @PreAuthorize("hasAnyAuthority('READ_MESSAGES')")
    public ResponseEntity<Page<Message>> findMessagesForDevice(@PathVariable UUID id,
                                                               @RequestParam(defaultValue = "0") @Min(0) int page,
                                                               @RequestParam(defaultValue = "12") @Min(1) @Max(20) int amount,
                                                               @RequestParam(required = false, defaultValue = "") String type,
                                                               @RequestParam(required = false, defaultValue = "") String text,
                                                               @RequestParam(required = false, defaultValue = "") String value,
                                                               @RequestParam(required = false, defaultValue = "") String timestamp,
                                                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        boolean hasDevice = false;
        if (user.hasRole("ROLE_TENANT")) {
            hasDevice = tenantRepository.doesUserHaveDevice(user.getId(), id);
        } else if (user.hasRole("ROLE_LANDLORD")) {
            hasDevice = landlordRepository.doesUserHaveDevice(user.getId(), id);
        }
        if (hasDevice) {
            return ResponseEntity.ok(deviceService.findMessagesForDevice(page, amount, id, type, text, value, timestamp));
        }  else {
            throw new ForbiddenDeviceAction("Cannot view messages for this device");
        }

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
