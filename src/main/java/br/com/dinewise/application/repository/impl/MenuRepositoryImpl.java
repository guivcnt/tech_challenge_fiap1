package br.com.dinewise.application.repository.impl;

import br.com.dinewise.application.entity.MenuEntity;
import br.com.dinewise.application.entity.RestaurantEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.MenuRepository;
import br.com.dinewise.domain.requests.menu.MenuRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Log4j2
@AllArgsConstructor
public class MenuRepositoryImpl implements MenuRepository {
    private final JdbcClient jdbcClient;

    @Override
    public MenuEntity create(MenuRequest request) throws DineWiseResponseError {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            LocalDateTime dateTime = LocalDateTime.now();
            Timestamp timestampDateTime = Timestamp.valueOf(dateTime);

            this.jdbcClient
                    .sql("""
                INSERT INTO menu (
                     name,
                     description,
                     price,
                     only_for_delivery,
                     image_path,
                     restaurant_id,
                     last_date_modified
                ) VALUES (
                    :name,
                    :description,
                    :price,
                    :onlyForDelivery,
                    :imagePath,
                    :restaurantId,
                    :lastDateModified
                )
                """)
                    .param("name", request.name())
                    .param("description", request.description())
                    .param("price", request.price())
                    .param("onlyForDelivery", request.onlyForDelivery())
                    .param("imagePath", request.imagePath())
                    .param("restaurantId", request.restaurantId())
                    .param("lastDateModified", timestampDateTime)
                    .update(keyHolder);

            Map<String, Object> keys = keyHolder.getKeys();
            Long id = (keys != null && keys.containsKey("id")) ? ((Number) keys.get("id")).longValue() : null;

            return new MenuEntity(id, request.name(), request.description(),
                    request.price(), request.onlyForDelivery(), request.imagePath(), request.restaurantId(), dateTime);

        } catch (Exception e){
            log.error("Erro ao cadastrar item no cardápio -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Error creating menu item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<MenuEntity> getAll() {
        return this.jdbcClient
                .sql("SELECT * FROM menu ORDER BY id")
                .query(MenuEntity.class)
                .list();
    }

    @Override
    public Optional<MenuEntity> get(Long id) throws DineWiseResponseError {
        try {
            return this.jdbcClient
                    .sql("""
                    SELECT id id, name name, description description, price price, only_for_delivery onlyForDelivery,
                           image_path imagePath, restaurant_id restaurantId, last_date_modified lastDateModified
                    FROM menu
                    WHERE id = :menuId
                """)
                    .param("menuId", id)
                    .query(MenuEntity.class)
                    .optional();
        }
        catch (Exception e) {
            log.error("Erro ao retornar um item do cardápio -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Error get menu item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<MenuEntity> update(Long idMenu, MenuRequest request) throws DineWiseResponseError {
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            Timestamp timestampDateTime = Timestamp.valueOf(dateTime);

            int rowsChanged = this.jdbcClient
                .sql("""
                    UPDATE menu
                    SET name = :name, description = :description, price = :price,
                        only_for_delivery = :onlyForDelivery, image_path = :imagePath,
                        restaurant_id = :restaurantId, last_date_modified = :lastDateModified
                    WHERE id = :idMenu
                """)
                    .param("name", request.name())
                    .param("description", request.description())
                    .param("price", request.price())
                    .param("onlyForDelivery", request.onlyForDelivery())
                    .param("imagePath", request.imagePath())
                    .param("restaurantId", request.restaurantId())
                    .param("idMenu", idMenu)
                    .param("lastDateModified", timestampDateTime)
                    .update();

            if (rowsChanged == 0) {
                return Optional.empty();
            }

            return Optional.of(new MenuEntity(
                    idMenu, request.name(), request.description(), request.price(),
                    request.onlyForDelivery(), request.imagePath(), request.restaurantId(), dateTime
                )
            );
        }
        catch (Exception e){
            log.error("Erro ao atualizar item do menu -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Error updating menu item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<MenuEntity> delete(Long id) throws DineWiseResponseError {
        try {
            int rowsChanged = this.jdbcClient
                .sql("DELETE FROM menu WHERE id = :idMenu")
                .param("idMenu", id)
                .update();

            if (rowsChanged == 0) {
                return Optional.empty();
            }

            return Optional.of(new MenuEntity(id, null, null, null, null, null, null, null));
        }
        catch (Exception e){
            log.error("Erro ao deletar item do menu -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Error deleting menu item", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

