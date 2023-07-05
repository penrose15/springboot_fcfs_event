package com.example.api.service;

import com.example.api.domain.repository.CouponRepository;
import com.example.api.domain.service.ApplyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(count)
                .isEqualTo(1L);
    }

    @Test //레이스컨디션 발생
    public void 여러명_응모() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0; i<threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply(userId);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        long count = couponRepository.count();
        assertThat(count)
                .isEqualTo(100L);
    }

    @Test
    public void kafka_여러명_응모() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0; i<threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply_kafka(userId);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Thread.sleep(10000); // consumer 가 완전히 작동할 때까지 대기
        // 배터리 없어서 테스트 못함 미래의 나야 화이팅

        long count = couponRepository.count();
        assertThat(count)
                .isEqualTo(100L);
    }

    @Test
    public void kafka_여러명_응모_한명당_한개만_발급() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0; i<threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply_kafka(1L);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        Thread.sleep(10000); // consumer 가 완전히 작동할 때까지 대기
        // 배터리 없어서 테스트 못함 미래의 나야 화이팅

        long count = couponRepository.count();
        assertThat(count)
                .isEqualTo(1L);
    }
}
