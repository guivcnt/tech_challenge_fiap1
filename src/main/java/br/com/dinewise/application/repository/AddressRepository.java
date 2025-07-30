package br.com.dinewise.application.repository;

import br.com.dinewise.application.entity.AddressEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.requests.address.AddressRequest;

import java.util.Optional;

public interface AddressRepository {
    AddressEntity create(AddressRequest request) throws DineWiseResponseError;

    Optional<AddressEntity> get(Long id) throws DineWiseResponseError;

    Optional<AddressEntity> delete(Long id) throws DineWiseResponseError;
}
