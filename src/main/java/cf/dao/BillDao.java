package cf.dao;

import cf.POJO.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillDao extends JpaRepository<Bill,Integer> {

    @Query("SELECT b FROM Bill b ORDER BY b.id desc")
    List<Bill> getAllBills();

    @Query("SELECT b FROM Bill b WHERE b.email=:username ORDER BY b.id desc")
    List<Bill> getBillByUsername(@Param("username") String currentUserEmail);
}
