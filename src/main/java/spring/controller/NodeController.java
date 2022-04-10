package spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.entities.NodeEntity;
import spring.service.NodeService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my_service/node")
public class NodeController {

    private final NodeService nodeService;

    @PostMapping()
    public NodeEntity saveNode(NodeEntity node) {
        return nodeService.saveNode(node);
    }

    @GetMapping("/{id}")
    public NodeEntity getNode(@PathVariable Long id) {
        return nodeService.getNode(id);
    }

    @DeleteMapping("/{id}")
    public void deleteNode(@PathVariable Long id) {
        nodeService.deleteNode(id);
    }

    @GetMapping("/radius")
    public NodeEntity[] getNodesInRadius(
            @RequestParam("lat") Double latitude,
            @RequestParam("lon") Double longitude,
            @RequestParam("radius") Double radius
    ) {
        Collection<NodeEntity> collection = nodeService.getNodesInRadius(latitude, longitude, radius);
        return collection.toArray(new NodeEntity[0]);
    }
}
