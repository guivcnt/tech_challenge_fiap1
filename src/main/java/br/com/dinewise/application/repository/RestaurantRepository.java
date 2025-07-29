package br.com.dinewise.application.repository;

import br.com.dinewise.application.entity.RestaurantEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.requests.restaurant.RestaurantRequest;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {
    RestaurantEntity create(RestaurantRequest request, Long idUser) throws DineWiseResponseError;
    List<RestaurantEntity> getAll();
    Optional<RestaurantEntity> get(Long id) throws DineWiseResponseError;
    Optional<RestaurantEntity> update(Long idRestaurant, RestaurantRequest request, Long idNewUser) throws DineWiseResponseError;
    Optional<RestaurantEntity> delete(Long id) throws DineWiseResponseError;
}
