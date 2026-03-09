package eiu.edu.vn.EquipmentBookingSystem.service;

import eiu.edu.vn.EquipmentBookingSystem.model.Booking;
import eiu.edu.vn.EquipmentBookingSystem.model.Account;
import eiu.edu.vn.EquipmentBookingSystem.model.Equipment;
import eiu.edu.vn.EquipmentBookingSystem.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EquipmentService equipmentService;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    // Tạo mới một lần mượn
    public Booking createBooking(Booking booking) {
        // Kiểm tra xem thiết bị có đủ số lượng không
        if (!equipmentService.isEquipmentAvailable(booking.getEquipment().getId(), booking.getQuantity())) {
            throw new IllegalArgumentException("Thiết bị không đủ số lượng");
        }

        booking.setBorrowedAt(LocalDateTime.now());
        booking.setStatus(Booking.BookingStatus.BORROWED);
        booking.setCreatedAt(LocalDateTime.now());

        // Cập nhật số lượng sẵn sàng của thiết bị
        equipmentService.borrowEquipment(booking.getEquipment().getId(), booking.getQuantity());

        return bookingRepository.save(booking);
    }

    // Trả thiết bị
    public Booking returnBooking(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            Booking b = booking.get();
            if (!b.getStatus().equals(Booking.BookingStatus.BORROWED)) {
                throw new IllegalArgumentException("Chỉ có thể trả thiết bị đang mượn");
            }

            b.setReturnedAt(LocalDateTime.now());
            b.setStatus(Booking.BookingStatus.RETURNED);

            // Cập nhật số lượng sẵn sàng của thiết bị
            equipmentService.returnEquipment(b.getEquipment().getId(), b.getQuantity());

            return bookingRepository.save(b);
        }
        throw new IllegalArgumentException("Không tìm thấy lần mượn");
    }

    public List<Booking> getBookingsByAccount(Account account) {
        return bookingRepository.findByAccountOrderByBorrowedAtDesc(account);
    }

    public List<Booking> getBookingsByEquipment(Equipment equipment) {
        return bookingRepository.findByEquipment(equipment);
    }

    public List<Booking> getActiveBorrowings(Account account) {
        List<Booking> bookings = bookingRepository.findByAccountOrderByBorrowedAtDesc(account);
        return bookings.stream()
                .filter(b -> b.getStatus().equals(Booking.BookingStatus.BORROWED))
                .toList();
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
}
