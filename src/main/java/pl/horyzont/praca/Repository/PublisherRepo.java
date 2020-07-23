package pl.horyzont.praca.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.horyzont.praca.Entity.Publisher;

public interface PublisherRepo extends JpaRepository <Publisher, Integer> {
}
