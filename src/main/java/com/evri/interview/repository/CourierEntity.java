package com.evri.interview.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "couriers")
public class CourierEntity {

    @Id
    @Column(name = "ID")
    private long id;
    @NonNull
    @Column(name = "FST_NME")
    private String firstName;
    @NonNull
    @Column(name = "LST_NME")
    private String lastName;
    @Column(name = "ACTV")
    private boolean active;

    public CourierEntity() {
    }
}
