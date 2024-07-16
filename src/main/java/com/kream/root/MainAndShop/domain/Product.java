package com.kream.root.MainAndShop.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kream.root.MainAndShop.domain.ProductInfo.ProductInfo;
import com.kream.root.MainAndShop.domain.ProductInfo.ProductInfoConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "product")
@SequenceGenerator(
    name = "product_seq_generator",
    sequenceName = "product_seq",
    allocationSize = 1
)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_generator")
    private Long prid;

    @Column(name= "product_name_kor", nullable = false)
    private String nameKor ;
    @Column(name="product_name_eng", nullable = false)
    private String nameEng ;
    @Column(name="product_category", nullable = false)
    private String category ;
    @Column(name="product_brand")
    public String brand ;
    @Column(name = "product_color", nullable = false)
    private String color ;
    @Column(name = "product_gender", nullable = false)
    private String gender ;
    @Column(name = "product_price", nullable = false)
    private int price ;

    @Column(name="product_info")
//    @Convert(converter = ProductInfoConverter.class)
    private String info ; // dto로 받아오기는 어려울까
    @Column(name="reg_date")
    private LocalDateTime regDate ;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference  // Add this lin
    private List<ProductImg> productImgs;


//    @JoinColumn(name = "prid")
//    @OneToOne(fetch = FetchType.LAZY)
//    private ProductImg productImg;


//    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
//    private  dealExtra;

}
