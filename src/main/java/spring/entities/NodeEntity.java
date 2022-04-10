package spring.entities;

import generated.Node;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "nodes")
@Accessors(chain = true)
public class NodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "username")
    private String userName;

    @Column(nullable = false, name = "longitude")
    private Double longitude;

    @Column(nullable = false, name = "latitude")
    private Double latitude;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "nodes_with_tag",
            joinColumns = {
                    @JoinColumn(name = "node_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id", referencedColumnName = "id")
            }
    )
    private Set<TagEntity> tags;

    public static NodeEntity createFromGenerated(Node node) {
        return new NodeEntity()
                .setId(node.getId().longValue())
                .setUserName(node.getUser())
                .setLongitude(node.getLon())
                .setLatitude(node.getLat())
                .setTags(node
                        .getTag()
                        .stream()
                        .map(TagEntity::createFromGenerated)
                        .collect(Collectors.toSet())
                );


    }
}
