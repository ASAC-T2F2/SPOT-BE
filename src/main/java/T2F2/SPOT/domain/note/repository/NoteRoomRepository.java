package T2F2.SPOT.domain.note.repository;

import T2F2.SPOT.domain.note.entity.NoteRoom;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRoomRepository extends JpaRepository<NoteRoom, Long> {

    NoteRoom findByPostAndGuest(Post post, User guest);

    @Query("SELECT nr FROM NoteRoom nr WHERE nr.guest.email = :guestEmail")
    List<NoteRoom> findByGuestEmail(@Param("guestEmail") String guestEmail);

    void deleteById(Long id);
}
