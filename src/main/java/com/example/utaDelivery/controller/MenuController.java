package com.example.utaDelivery.controller;

import com.example.utaDelivery.DTO.MenuDTO;
import com.example.utaDelivery.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDTO> getMenuById(@PathVariable Long menuId) {
        MenuDTO menuDTO = menuService.getMenuById(menuId);
        return ResponseEntity.ok(menuDTO);
    }

    @GetMapping
    public ResponseEntity<List<MenuDTO>> getAllMenus() {
        List<MenuDTO> menuDTOs = menuService.getAllMenus();
        return ResponseEntity.ok(menuDTOs);
    }

    @PostMapping
    public ResponseEntity<MenuDTO> createMenu(@RequestBody MenuDTO menuDTO) {
        MenuDTO createdMenu = menuService.createMenu(menuDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenu);
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<MenuDTO> updateMenu(@PathVariable Long menuId, @RequestBody MenuDTO menuDTO) {
        MenuDTO updatedMenu = menuService.updateMenu(menuId, menuDTO);
        return ResponseEntity.ok(updatedMenu);
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.noContent().build();
    }
}
