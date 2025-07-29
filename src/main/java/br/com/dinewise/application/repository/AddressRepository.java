package br.com.dinewise.application.repository;

import br.com.dinewise.application.entity.AddressEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.requests.address.AddressRequest;

public interface AddressRepository {
    AddressEntity create(AddressRequest request) throws DineWiseResponseError;
}
