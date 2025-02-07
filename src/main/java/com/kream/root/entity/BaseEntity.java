package com.kream.root.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@MappedSuperclass
@EntityListeners(
        value = { AuditingEntityListener.class }
)
@Getter
abstract class BaseEntity {
    @CreatedDate
    @Column(name = "ub_date", updatable = false)
    private LocalDate ub_date;

}