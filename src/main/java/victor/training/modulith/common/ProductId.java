package victor.training.modulith.common;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProductId(long id) {
}
