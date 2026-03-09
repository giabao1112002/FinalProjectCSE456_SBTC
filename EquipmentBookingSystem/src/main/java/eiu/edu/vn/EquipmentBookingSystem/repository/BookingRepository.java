package eiu.edu.vn.EquipmentBookingSystem.repository;

import eiu.edu.vn.EquipmentBookingSystem.model.Booking;
import eiu.edu.vn.EquipmentBookingSystem.model.Account;
import eiu.edu.vn.EquipmentBookingSystem.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByAccount(Account account);
    List<Booking> findByEquipment(Equipment equipment);
    List<Booking> findByStatus(Booking.BookingStatus status);
    List<Booking> findByAccountOrderByBorrowedAtDesc(Account account);
}
