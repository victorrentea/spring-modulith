package victor.training.modulith.shared;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;

@Embeddable
public record ProductId(long id) {
}
