package aaagt.cloudservice.user.repository;

import aaagt.cloudservice.user.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    @Query(value = """
            select t from UserToken t inner join User u\s
            on t.user.id = u.id\s
            where u.id = :id and t.revoked = false\s
            """)
    List<UserToken> findAllValidTokenByUser(Long id);

    Optional<UserToken> findByToken(String token);

}
