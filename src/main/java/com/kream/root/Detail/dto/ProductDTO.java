package com.kream.root.Detail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private long prId;

    private String nameKor;
    private String nameEng;
    private String category;
    private String brand;
    private String color;
    private long price;
    private String info;
    private String gender;
    private LocalDateTime regDate;
    private List<ProductImgDTO> productImgs;
}