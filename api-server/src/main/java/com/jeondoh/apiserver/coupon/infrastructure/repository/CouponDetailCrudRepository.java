package com.jeondoh.apiserver.coupon.infrastructure.repository;

import com.jeondoh.apiserver.coupon.domain.model.CouponDetailHash;
import org.springframework.data.repository.CrudRepository;

public interface CouponDetailCrudRepository extends CrudRepository<CouponDetailHash, String> {
}
