package br.com.dinewise.application.repository;

import br.com.dinewise.application.entity.MenuEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.requests.menu.MenuRequest;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {
    MenuEntity create(MenuRequest request) throws DineWiseResponseError;
    List<MenuEntity> getAll();
    Optional<MenuEntity> get(Long id) throws DineWiseResponseError;
    Optional<MenuEntity> update(Long idMenu, MenuRequest request) throws DineWiseResponseError;
    Optional<MenuEntity> delete(Long id) throws DineWiseResponseError;
}
