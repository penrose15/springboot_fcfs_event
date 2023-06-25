package com.example.api.domain.service;

import com.example.api.domain.Coupon;
import com.example.api.domain.repository.CouponCountRepository;
import com.example.api.domain.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;

    public void apply(Long userId) {
        long count = couponCountRepository.increment();
        if(count > 100) {
            return;
        }
        couponRepository.save(new Coupon(userId));
    }
}
