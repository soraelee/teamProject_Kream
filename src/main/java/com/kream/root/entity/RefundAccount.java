package com.kream.root.entity;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.kream.root.Login.model.UserListDTO;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "REFUND_ACCOUNT")
public class RefundAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refundAccountSeqGen")
    @SequenceGenerator(name = "refundAccountSeqGen", sequenceName = "refund_account_seq", allocationSize = 1)
    @Column(name = "RAID")
    private int raid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ULID", unique = true)
    private UserListDTO user;

    @Column(name = "ACCOUNT_NUMBER")
    @NotNull
    @Size(max = 50)
    private String accountNumber;

    @Column(name = "BANK_CODE")
    @NotNull
    @Size(max = 20)
    private String bankCode;

    @Column(name = "ACCOUNT_HOLDER")
    @NotNull
    @Size(max = 100)
    private String accountHolder;

    @Column(name = "CONTACT_NUMBER")
    @NotNull
    @Size(max = 20)
    private String contactNumber;

    public RefundAccount(UserListDTO user, @NotNull @Size(max = 50) String accountNumber,
                         @NotNull @Size(max = 20) String bankCode, @NotNull @Size(max = 100) String accountHolder,
                         @NotNull @Size(max = 20) String contactNumber) {
        this.user = user;
        this.accountNumber = accountNumber;
        this.bankCode = bankCode;
        this.accountHolder = accountHolder;
        this.contactNumber = contactNumber;
    }
}