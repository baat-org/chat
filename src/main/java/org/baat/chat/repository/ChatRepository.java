package org.baat.chat.repository;

import org.baat.channel.repository.entity.DirectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<DirectEntity, DirectEntity> {

    List<DirectEntity> findByFirstUserId(long firstUserId);

    List<DirectEntity> findBySecondUserId(long secondUserId);
}
