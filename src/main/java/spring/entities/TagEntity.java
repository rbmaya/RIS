package spring.entities;

import generated.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tags")
@Accessors(chain = true)
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "key")
    private String key;

    @Column(nullable = false, name = "value")
    private String value;

    public static TagEntity createFromGenerated(Tag tag) {
        return new TagEntity()
                .setKey(tag.getK())
                .setValue(tag.getV());
    }
}
