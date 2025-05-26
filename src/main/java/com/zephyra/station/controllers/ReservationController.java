package com.zephyra.station.controllers;

import com.zephyra.station.dto.BookRequest;
import com.zephyra.station.dto.ReservationDTO;
import com.zephyra.station.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    ReservationService reservationService;
    @PostMapping("/book")
    public ResponseEntity<ReservationDTO> book(@Validated @RequestBody ReservationDTO req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String supabaseId = jwt.getClaimAsString("sub");
        return ResponseEntity.ok(reservationService.book(req,supabaseId));
    }
    @GetMapping("/getall")
    public List<ReservationDTO> getUserReservations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String supabaseId = jwt.getClaimAsString("sub");
        return reservationService.getUserReservations(supabaseId);
    }
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String supabaseId = jwt.getClaimAsString("sub");
        reservationService.cancelReservation(id, supabaseId);
        return ResponseEntity.noContent().build();
    }
}
