package com.college.sms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type;
    private Long id;
    private String email;
    private List<String> roles;
}
