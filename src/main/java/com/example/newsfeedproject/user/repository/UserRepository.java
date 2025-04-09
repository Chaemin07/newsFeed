/*User 엔티티에 대한 데이터베이스 접근을 담당하는 JPA 레포지토리 인터페이스.
 */

package com.example.newsfeedproject.user.repository;

import com.example.newsfeedproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmailAndDeletedFalse(String email);
}
