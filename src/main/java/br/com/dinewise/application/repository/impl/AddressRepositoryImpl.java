package br.com.dinewise.application.repository.impl;

import br.com.dinewise.application.entity.AddressEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.AddressRepository;
import br.com.dinewise.domain.requests.address.AddressRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Repository
@Log4j2
@AllArgsConstructor
public class AddressRepositoryImpl implements AddressRepository {
    private final JdbcClient jdbcClient;

    @Override
    public AddressEntity create(AddressRequest request) throws DineWiseResponseError {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            LocalDateTime dateTime = LocalDateTime.now();
            Timestamp timestampDateTime = Timestamp.valueOf(dateTime);

            this.jdbcClient
                    .sql("INSERT INTO addresses (public_place, house_number, complement, neighborhood, city, state, zip_code)" +
                            "VALUES (:publicPlace, :houseNumber, :complement, :neighborhood, :city, :state, :zipCode)")
                    .param("publicPlace", request.street())
                    .param("houseNumber", request.houseNumber())
                    .param("complement", request.complement())
                    .param("neighborhood", request.neighborhood())
                    .param("city", request.city())
                    .param("state", request.state())
                    .param("zipCode", request.zipCode())
                    .param("lastDateModified", timestampDateTime)
                    .update(keyHolder);

            Map<String, Object> keys = keyHolder.getKeys();
            Long id = (keys != null && keys.containsKey("id")) ? ((Number) keys.get("id")).longValue() : null;

            return new AddressEntity(id, request.street(), request.houseNumber(), request.complement(), request.neighborhood(), request.city(), request.state(), request.zipCode(), dateTime);

        } catch (Exception e) {
            log.error("Erro ao cadastrar endereço -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Error creating address", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
