package com.evri.interview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
@Entity
@Table(name = "couriers")
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "FST_NME")
    @NotBlank(message = "First name must be provided.")
    @Size(max = 64, message = "First name should be less then 64 characters.")
    private String firstName;

    @NotBlank(message = "Last name must be provided.")
    @Size(max = 64, message = "Last name should be less then 64 characters.")
    @Column(name = "LST_NME")
    private String lastName;

    @NotNull(message = "Active status must be provided.")
    @Column(name = "ACTV")
    private boolean active;
}