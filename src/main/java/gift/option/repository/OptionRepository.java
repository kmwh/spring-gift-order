package gift.option.repository;

import gift.option.entity.Option;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByProductId(Long productId);
    Optional<Option> findByIdAndProductId(Long optionId, Long productId);
}
