package com.sparta.lafesta.notification.repository;

import com.sparta.lafesta.notification.entity.Notification;
import com.sparta.lafesta.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>  {

    List<Notification> findAllByFollower(User user);

    void deleteByExpirationTimeBefore(LocalDateTime now);

}
