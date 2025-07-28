package br.com.dinewise.application.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeEntity {
    private Long id;
    private String type;
    private LocalDateTime lastDateModified;
}
