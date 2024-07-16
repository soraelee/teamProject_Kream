package com.kream.root.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "delivery")
@SequenceGenerator(
        name = "delivery_seq_generator",
        sequenceName = "delivery_seq",
        allocationSize = 1
)
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delivery_seq_generator")
    private Long deliveryId;

    @Column(nullable = false)
    private String deliveryStatus;

    @Column(nullable = false)
    private String deliveryAddress;

    private LocalDateTime deliveryDate;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;
}
