package com.weljak.splitter.utils.mapper.config

import org.mapstruct.MapperConfig
import org.mapstruct.ReportingPolicy

@MapperConfig(
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
class GlobalMapperConfig {
}