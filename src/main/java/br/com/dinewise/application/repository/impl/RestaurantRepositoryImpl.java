package br.com.dinewise.application.repository.impl;

import br.com.dinewise.application.entity.RestaurantEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.AddressRepository;
import br.com.dinewise.application.repository.RestaurantRepository;
import br.com.dinewise.domain.requests.restaurant.RestaurantRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Log4j2
@AllArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {
    private final JdbcClient jdbcClient;
    private final AddressRepository addressRepository;

    @Transactional
    @Override
    public RestaurantEntity create(RestaurantRequest request, Long idUser) throws DineWiseResponseError {
        try {
            var addressEntity = addressRepository.create(request.address());

            KeyHolder keyHolder = new GeneratedKeyHolder();

            LocalDateTime dateTime = LocalDateTime.now();
            Timestamp timestampDateTime = Timestamp.valueOf(dateTime);

            this.jdbcClient
                    .sql("""
                    INSERT INTO restaurants (
                         name,
                         type_cuisine,
                         operation_hours,
                         user_id_owner,
                         address_id,
                         last_date_modified
                    ) VALUES (
                        :name,
                        :typeCuisine,
                        :operationHours,
                        :idUser,
                        :addressId,
                        :lastDateModified
                    )
                    """)
                    .param("name", request.name())
                    .param("typeCuisine", request.typeCuisine())
                    .param("operationHours", request.operationHours())
                    .param("idUser", idUser)
                    .param("addressId", addressEntity.getId())
                    .param("lastDateModified", timestampDateTime)
                    .update(keyHolder);

            Map<String, Object> keys = keyHolder.getKeys();
            Long id = (keys != null && keys.containsKey("id")) ? ((Number) keys.get("id")).longValue() : null;

            return new RestaurantEntity(id, request.name(), request.typeCuisine(), request.operationHours(), idUser, addressEntity.getId(), dateTime);

        } catch (DuplicateKeyException e){
            log.error("Restaurante já existente -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Restaurant already exists", HttpStatus.CONFLICT);

        } catch (Exception e){
            log.error("Erro ao cadastrar restaurante -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Error creating restaurant", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<RestaurantEntity> getAll() {
        return this.jdbcClient
                .sql("SELECT * FROM restaurants ORDER BY id")
                .query(RestaurantEntity.class)
                .list();
    }

    @Override
    public Optional<RestaurantEntity> get(Long id) throws DineWiseResponseError {
        try {
            return this.jdbcClient
                    .sql("""
                        SELECT id id, name name, type_cuisine typeCuisine, operation_hours operationHours,
                               user_id_owner userIdOwner, address_id addressId, last_date_modified lastDateModified
                        FROM restaurants
                        WHERE id = :restaurantId
                    """)
                    .param("restaurantId", id)
                    .query(RestaurantEntity.class)
                    .optional();
        }
        catch (Exception e) {
            log.error("Erro ao retornar endereço -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Error get restaurant", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public Optional<RestaurantEntity> update(Long idRestaurant, RestaurantRequest request, Long idNewUser) throws DineWiseResponseError {
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            Timestamp timestampDateTime = Timestamp.valueOf(dateTime);

            int rowsChanged = this.jdbcClient
                    .sql("""
                UPDATE restaurants
                SET name = :name, type_cuisine = :typeCuisine, operation_hours = :operationHours,
                    user_id_owner = :userId, last_date_modified = :lastDateModified
                WHERE id = :restaurantId
            """)
                .param("name", request.name())
                .param("typeCuisine", request.typeCuisine())
                .param("operationHours", request.operationHours())
                .param("userId", idNewUser)
                .param("restaurantId", idRestaurant)
                .param("lastDateModified", timestampDateTime)
                .update();

            this.jdbcClient
                    .sql("""
                UPDATE addresses
                SET public_place = :publicPlace, house_number = :houseNumber, complement = :complement,
                    neighborhood = :neighborhood, city = :city, state = :state, zip_code = :zipCode
                FROM restaurants re
                WHERE re.id = :restaurantId and
                      re.address_id = addresses.id
            """)
                    .param("publicPlace", request.address().street())
                    .param("houseNumber", request.address().houseNumber())
                    .param("complement", request.address().complement())
                    .param("neighborhood", request.address().neighborhood())
                    .param("city", request.address().city())
                    .param("state", request.address().state())
                    .param("zipCode", request.address().zipCode())
                    .param("restaurantId", idRestaurant)
                    .update();

            if (rowsChanged == 0) {
                return Optional.empty();
            }

            return Optional.of(new RestaurantEntity(
                    idRestaurant,
                    request.name(),
                    request.typeCuisine(),
                    request.operationHours(),
                    idNewUser,
                    null,
                    dateTime
                )
            );
        }
        catch (Exception e){
            log.error("Erro ao atualizar restaurante -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Error updating restaurant", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<RestaurantEntity> delete(Long id) throws DineWiseResponseError {
        try {
            int rowsChanged = this.jdbcClient
                    .sql("""
                        DELETE FROM addresses
                        USING restaurants
                        WHERE restaurants.address_id = addresses.id
                          AND restaurants.id = :id;
                    """)
                    .param("id", id)
                    .update();

            if (rowsChanged == 0) {
                return Optional.empty();
            }

            return Optional.of(new RestaurantEntity(id, null, null, null, null, null, null));
        }
        catch (Exception e){
            log.error("Erro ao deletar restaurante -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Error deleting restaurant", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
