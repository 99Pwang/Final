package com.example.springboot.controller.dto;

import com.example.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * parameters for accepting login requests from the frontend
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}
