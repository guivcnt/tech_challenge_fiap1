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
public class AddressEntity {
    private Long id;
    private String publicPlace;
    private String houseNumber;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private LocalDateTime lastDateModified;
}
