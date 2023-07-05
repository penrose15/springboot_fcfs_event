package com.example.api.domain.service;

import com.example.api.domain.AppliedUserRepository;
import com.example.api.domain.Coupon;
import com.example.api.domain.repository.CouponCountRepository;
import com.example.api.domain.repository.CouponRepository;
import com.example.api.producer.CouponCreateProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;

    public void apply(Long userId) {
        long count = couponCountRepository.increment();
        if(count > 100) {
            return;
        }
        couponRepository.save(new Coupon(userId));
    }

    public void apply_kafka(Long userId) {
        Long apply = appliedUserRepository.add(userId);

        if(apply != 1) {
            return;
        }

        long count = couponCountRepository.increment();

        if(count > 100) {
            return;
        }

        couponCreateProducer.create(userId);
    }
}
