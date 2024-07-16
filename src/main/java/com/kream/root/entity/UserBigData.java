package com.kream.root.entity;

import com.kream.root.Login.model.UserListDTO;
import com.kream.root.MainAndShop.domain.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@Table(name = "User_Bigdata")
@SequenceGenerator(
        name="user_bigdata_seq_generator", sequenceName = "user_bigdata_seq", allocationSize = 1
)
@NoArgsConstructor
@AllArgsConstructor
public class UserBigData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_bigdata_seq_generator")
    private Long ub_id;

//    @Column
//    private LocalDateTime ub_date;

    @ManyToOne(targetEntity = UserListDTO.class, fetch = FetchType.LAZY)
    @JoinColumn(name="ub_ulid", referencedColumnName = "ulid")
    private UserListDTO userListDTO;


    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY) //cascade = CascadeType.ALL 지우기
    @JoinColumn(name = "ub_prid", referencedColumnName = "prid")
    private Product product;

    @Column
    private int ub_clickCount;
}


