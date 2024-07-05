package com.example.utaDelivery.impl;

import com.example.utaDelivery.DTO.MenuDTO;
import com.example.utaDelivery.Entity.Menu;
import com.example.utaDelivery.Entity.Restaurant;
import com.example.utaDelivery.exception.ResourceNotFoundException;
import com.example.utaDelivery.repository.MenuRepository;
import com.example.utaDelivery.repository.RestaurantRepository;
import com.example.utaDelivery.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public MenuDTO getMenuById(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
        return convertToDTO(menu);
    }

    @Override
    public List<MenuDTO> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public MenuDTO createMenu(MenuDTO menuDTO) {
        Menu menu = convertToEntity(menuDTO);
        Menu savedMenu = menuRepository.save(menu);
        return convertToDTO(savedMenu);
    }

    @Override
    public MenuDTO updateMenu(Long menuId, MenuDTO menuDTO) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
        menu.setItemName(menuDTO.getItemName());
        menu.setDescription(menuDTO.getDescription());
        menu.setPrice(menuDTO.getPrice());
        // Assuming restaurant cannot be changed during update
        Menu updatedMenu = menuRepository.save(menu);
        return convertToDTO(updatedMenu);
    }

    @Override
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
        menuRepository.delete(menu);
    }

    private MenuDTO convertToDTO(Menu menu) {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setMenuId(menu.getMenuId());
        menuDTO.setRestaurantId(menu.getRestaurant().getRestaurantId());
        menuDTO.setItemName(menu.getItemName());
        menuDTO.setDescription(menu.getDescription());
        menuDTO.setPrice(menu.getPrice());
        return menuDTO;
    }

    private Menu convertToEntity(MenuDTO menuDTO) {
        Menu menu = new Menu();
        menu.setMenuId(menuDTO.getMenuId());
        Restaurant restaurant = restaurantRepository.findById(menuDTO.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        menu.setRestaurant(restaurant);
        menu.setItemName(menuDTO.getItemName());
        menu.setDescription(menuDTO.getDescription());
        menu.setPrice(menuDTO.getPrice());
        return menu;
    }
}
