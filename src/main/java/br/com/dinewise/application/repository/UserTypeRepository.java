package br.com.dinewise.application.repository;

import br.com.dinewise.application.entity.UserTypeEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.requests.user.UserTypeRequest;

import java.util.List;
import java.util.Optional;

public interface UserTypeRepository {
    UserTypeEntity create(UserTypeRequest request) throws DineWiseResponseError;

    Optional<UserTypeEntity> read(UserTypeRequest request);

    List<UserTypeEntity> readAll();

    Optional<UserTypeEntity> update(Long id, UserTypeRequest request) throws DineWiseResponseError;

    Optional<UserTypeEntity> delete(Long id) throws DineWiseResponseError;
}
