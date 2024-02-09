package cf.dao;

import cf.POJO.User;
import cf.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDao extends JpaRepository<User,Integer> {
    User findByEmail(String email);
    @Query("SELECT new cf.wrapper.UserWrapper(u.id, u.name, u.email, u.phone, u.status) FROM User u WHERE u.role='user'")
    List<UserWrapper> getAllUser();

    @Query("SELECT u.email FROM User u WHERE u.role='admin'")
    List<String> getAllAdminEmails();

    @Modifying
    @Query("UPDATE User u SET u.status=:status WHERE u.id=:id")
    Integer updateStatus(@Param("status") String status,@Param("id") Integer id);

}
