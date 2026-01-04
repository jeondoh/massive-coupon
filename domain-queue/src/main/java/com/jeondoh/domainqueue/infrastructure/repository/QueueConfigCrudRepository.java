package com.jeondoh.domainqueue.infrastructure.repository;

import com.jeondoh.domainqueue.domain.model.QueueConfigHash;
import org.springframework.data.repository.CrudRepository;

public interface QueueConfigCrudRepository extends CrudRepository<QueueConfigHash, String> {
}
