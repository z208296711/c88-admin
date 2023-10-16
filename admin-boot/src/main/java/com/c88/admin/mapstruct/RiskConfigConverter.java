package com.c88.admin.mapstruct;

import com.c88.admin.dto.RiskConfigDTO;
import com.c88.admin.pojo.entity.RiskConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RiskConfigConverter {

    List<RiskConfigDTO> toVO(List<RiskConfig> entity);
    RiskConfigDTO toVO(RiskConfig entity);

}
