package com.example.springboot.service;

import com.example.springboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  service class
 * </p>
 *
 * @author
 * 
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
