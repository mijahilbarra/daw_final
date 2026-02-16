package com.cibertec.logistics.client;

import com.cibertec.logistics.dto.CategoryDto;
import com.cibertec.logistics.dto.ClientDto;
import com.cibertec.logistics.dto.StatusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-masterdata")
public interface MasterdataClient {

    @GetMapping("/clients")
    List<ClientDto> findClients(@RequestParam("id") String id);

    @GetMapping("/categories")
    List<CategoryDto> findCategories(@RequestParam("id") String id);

    @GetMapping("/statuses")
    List<StatusDto> findStatuses(@RequestParam("id") String id);
}
