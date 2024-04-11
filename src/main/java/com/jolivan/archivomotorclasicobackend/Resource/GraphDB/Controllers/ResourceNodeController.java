package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers;


import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Exceptions.ResourceNodeNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
//@RequestMapping("resourcenode")
public class ResourceNodeController {

    private final ResourceNodeService ResourceNodeService;

    @Autowired
    public ResourceNodeController(ResourceNodeService ResourceNodeService) {
        this.ResourceNodeService = ResourceNodeService;
    }

    @GetMapping("/resourcenodes")
    public List<ResourceNode> getAllResourceNodes(){
        List<ResourceNode> list = ResourceNodeService.getAllResourceNodes();
        return list;
    }

    @GetMapping("/resourcenodes/{resourceNodeId}")
    public ResourceNode getResourceNodeById(@PathVariable String resourceNodeId) throws ResourceNodeNotFound {
        return ResourceNodeService.getResourceNodeById(resourceNodeId);
    }

    @PostMapping(value = "/resourcenodes", consumes = {"application/json"})
    public ResourceNode addResourceNode(@RequestBody ResourceNode ResourceNode){
        return ResourceNodeService.addResourceNode(ResourceNode);
    }

    @PutMapping
    public ResourceNode updateResourceNode(@RequestBody ResourceNode ResourceNode)  {
        return ResourceNodeService.updateResourceNode(ResourceNode);
    }

    @DeleteMapping("/resourcenodes/{resourceNodeId}")
    public void deleteResourceNode(@PathVariable String resourceNodeId){
        ResourceNodeService.deleteResourceNode(Long.valueOf(resourceNodeId));
    }

    @GetMapping("/resourcenodes/test/query")
    public List<ResourceNode> searchResources(@RequestParam(name = "place", required = false) String place,
                                             @RequestParam(name = "date", required = false) List<ZonedDateTime> dates,
                                             @RequestParam(name = "competition", required = false) List<String> competitions,
                                             @RequestParam(name = "magazine", required = false) List<String> magazines){
        return ResourceNodeService.searchResources(place, dates, competitions, magazines);
    }

}
