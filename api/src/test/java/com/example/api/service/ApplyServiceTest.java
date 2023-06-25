package com.example.api.service;

import com.example.api.domain.repository.CouponRepository;
import com.example.api.domain.service.ApplyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplyServiceTest {
    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    public void 한번만_응모() {
        applyService.apply(1L);
        long count = couponRepository.count();

        Assertions.assertThat(count)
                .isEqualTo(1L);
    }
}
