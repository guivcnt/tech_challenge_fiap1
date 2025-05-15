package br.com.dinewise.application.repository.impl;

import br.com.dinewise.application.entity.UserEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.application.repository.UserRepository;
import br.com.dinewise.domain.requests.LoginRequest;
import br.com.dinewise.domain.requests.UserRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("ALL")
@Repository
@Log4j2
public class UserRepositoryImpl implements UserRepository {

    private final JdbcClient jdbcClient;

    public UserRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<UserEntity> login(LoginRequest request) throws DineWiseResponseError {
        try {
            var login = request.login();
            var password = request.password();

// TODO: retirar * from
            return this.jdbcClient
                    .sql("SELECT * FROM users WHERE login = :login AND password = :password")
                    .param("login", login)
                    .param("password", password)
                    .query(UserEntity.class)
                    .optional();
        }
        catch (Exception e) {
            log.error("Erro ao logar usuário -> {}", e.getMessage());
            throw new DineWiseResponseError("Error logging in", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserEntity createUser(UserRequest request) throws DineWiseResponseError {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            LocalDateTime dateTime = LocalDateTime.now();
            Timestamp timestampDateTime = Timestamp.valueOf(dateTime);

// TODO: validar email/login/qq campo vazio.
            this.jdbcClient
                    .sql("INSERT INTO users (name, email, login, password, user_type, last_date_modified) VALUES (:name, :email, :login, :password, :userType, :lastDateModified)")
                    .param("name", request.name())
                    .param("email", request.email())
                    .param("login", request.login())
                    .param("password", request.password())
                    .param("userType", request.userType())
                    .param("lastDateModified", timestampDateTime)
                    .update(keyHolder);

            Map<String, Object> keys = keyHolder.getKeys();
            Long userId = (keys != null && keys.containsKey("id")) ? ((Number) keys.get("id")).longValue() : null;

            this.jdbcClient
                    .sql("INSERT INTO addresses (user_id, public_place, house_number, complement,  neighborhood, city, state, zip_code) " +
                            "VALUES (:userId, :publicPlace, :houseNumber, :complement, :neighborhood, :city, :state, :zipCode)")
                    .param("userId", userId)
                    .param("publicPlace", request.street())
                    .param("houseNumber", request.houseNumber())
                    .param("complement", request.complement())
                    .param("neighborhood", request.neighborhood())
                    .param("city", request.city())
                    .param("state", request.state())
                    .param("zipCode", request.zipCode())
                    .update();

            return new UserEntity(userId, request.name(), request.email(), request.login(), request.password(), request.userType(), dateTime);
        }
        catch (Exception e){
            log.error("Erro ao cadastrar usuário -> {}", e.getMessage());
            throw new DineWiseResponseError("Error creating user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<UserEntity> updateUser(Long userId, UserRequest request) throws DineWiseResponseError {
        try {
// TODO: commit da transação?
            int rowsChanged = this.jdbcClient
                    .sql("""
                UPDATE users
                SET name = :name, email = :email, login = :login, password = :password, 
                    user_type = :userType, last_date_modified = CURRENT_TIMESTAMP
                WHERE id = :userId
            """)
                    .param("name", request.name())
                    .param("email", request.email())
                    .param("login", request.login())
                    .param("password", request.password())
                    .param("userType", request.userType())
                    .param("userId", userId)
                    .update();


            // atualizar o end. pela pk user_id
            this.jdbcClient
                    .sql("""
                UPDATE addresses
                SET public_place = :publicPlace, house_number = :houseNumber, complement = :complement, 
                    neighborhood = :neighborhood, city = :city, state = :state, zip_code = :zipCode
                WHERE user_id = :userId
            """)
                    .param("publicPlace", request.street())
                    .param("houseNumber", request.houseNumber())
                    .param("complement", request.complement())
                    .param("neighborhood", request.neighborhood())
                    .param("city", request.city())
                    .param("state", request.state())
                    .param("zipCode", request.zipCode())
                    .param("userId", userId)
                    .update();

            if (rowsChanged == 0) {
                return Optional.empty();
            }

            return Optional.of(new UserEntity(
                    userId,
                    request.name(),
                    request.email(),
                    request.login(),
                    request.password(),
                    request.userType(),
                    LocalDateTime.now()
            ));
        }
        catch (Exception e){
            log.error("Erro ao atualizar usuário -> {}", e.getMessage());
            throw new DineWiseResponseError("Error updating user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<UserEntity> deleteUser(Long userId) throws DineWiseResponseError {
        try {
            int rowsChanged = this.jdbcClient
                    .sql("DELETE FROM users WHERE id = :userId")
                    .param("userId", userId)
                    .update();

            if (rowsChanged == 0) {
                return Optional.empty();
            }

            return Optional.of(new UserEntity(userId, null, null, null, null, null, null));
        }
        catch (Exception e){
            log.error("Erro ao deletar usuário -> {}", e.getMessage());
           throw new DineWiseResponseError("Error deleting user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
