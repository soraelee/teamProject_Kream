package com.kream.root.MainAndShop.repository.ProductRecommend;

import com.kream.root.MainAndShop.dto.OneProductDTO;
import com.kream.root.MainAndShop.dto.brandDTO;

import java.util.List;

public interface Recommend {
    //추천을 위한 간단한 연산 필요할 경우
    //브랜드 별 추천
    List<brandDTO> getBrandCnt();

    // 이미지와 상품데이터의 결합
    List<OneProductDTO> getOneProduct(List<Long> pridList);
}
