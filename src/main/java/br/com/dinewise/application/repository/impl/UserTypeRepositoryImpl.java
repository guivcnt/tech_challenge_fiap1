package br.com.dinewise.application.repository.impl;

import br.com.dinewise.application.entity.UserEntity;
import br.com.dinewise.application.entity.UserTypeEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.UserTypeRepository;
import br.com.dinewise.domain.requests.user.UserTypeRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
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
public class UserTypeRepositoryImpl implements UserTypeRepository {
    private final JdbcClient jdbcClient;

    @Override
    public UserTypeEntity create(UserTypeRequest request) throws DineWiseResponseError {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            LocalDateTime dateTime = LocalDateTime.now();
            Timestamp timestampDateTime = Timestamp.valueOf(dateTime);

            this.jdbcClient
                    .sql("INSERT INTO user_types (type, last_date_modified) VALUES (:type, :lastDateModified)")
                    .param("type", request.userType())
                    .param("lastDateModified", timestampDateTime)
                    .update(keyHolder);

            Map<String, Object> keys = keyHolder.getKeys();
            Long typeId = (keys != null && keys.containsKey("id")) ? ((Number) keys.get("id")).longValue() : null;

            return new UserTypeEntity(typeId, request.userType(), dateTime);

        } catch (DuplicateKeyException e) {
            log.error("Tipo de usuário já cadastrado -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("User type already exists", HttpStatus.CONFLICT);

        } catch (Exception e){
            log.error("Erro ao cadastrar o tipo do usuário -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Error creating user type", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<UserTypeEntity> read(UserTypeRequest request) {
        return this.jdbcClient
                .sql("SELECT id  FROM user_types WHERE type = :type")
                .param("type", request.userType())
                .query(UserTypeEntity.class)
                .optional();
    }

    @Override
    public List<UserTypeEntity> readAll() {
        return this.jdbcClient
                .sql("SELECT * FROM user_types ORDER BY id")
                .query(UserTypeEntity.class)
                .list();
    }

    @Override
    public Optional<UserTypeEntity> update(Long id, UserTypeRequest request) throws DineWiseResponseError {
        try {
            int rowsChanged = this.jdbcClient
                    .sql("""
                UPDATE user_types
                SET type = :newType,
                    last_date_modified = CURRENT_TIMESTAMP
                WHERE id = :id
            """)
                    .param("newType", request.userType())
                    .param("id", id)
                    .update();

            if (rowsChanged == 0) {
                return Optional.empty();
            }

            return this.read(request);

        } catch (DuplicateKeyException e) {
            log.error("Tipo de usuário já cadastrado -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("User type already exists", HttpStatus.CONFLICT);

        }  catch (Exception e) {
            log.error("Erro ao atualizar o tipo -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Error updating type", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<UserTypeEntity> delete(Long id) throws DineWiseResponseError {
        try {
            int rowsChanged = this.jdbcClient
                    .sql("DELETE FROM user_types WHERE id = :id")
                    .param("id", id)
                    .update();

            if (rowsChanged == 0) {
                return Optional.empty();
            }

            return Optional.of(new UserTypeEntity(id, null, null));

        } catch (DataIntegrityViolationException e) {
            log.error("O tipo de usuário está sendo utilizado, e por isso não podemos excluir -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("User type is being used", HttpStatus.CONFLICT);

        }  catch (Exception e) {
            log.error("Erro ao deletar tipo do usuário -> {} / {}", e.getMessage(), e.getCause());
            throw new DineWiseResponseError("Error deleting user type", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
