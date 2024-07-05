package com.example.utaDelivery.service;

import com.example.utaDelivery.DTO.MenuDTO;

import java.util.List;

public interface MenuService {
    MenuDTO getMenuById(Long menuId);
    List<MenuDTO> getAllMenus();
    MenuDTO createMenu(MenuDTO menuDTO);
    MenuDTO updateMenu(Long menuId, MenuDTO menuDTO);
    void deleteMenu(Long menuId);
}
