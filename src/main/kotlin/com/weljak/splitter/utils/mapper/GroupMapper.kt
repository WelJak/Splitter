package com.weljak.splitter.utils.mapper

import com.weljak.splitter.webapi.controller.group.CreateGroupForm
import com.weljak.splitter.domain.model.group.Group
import com.weljak.splitter.domain.model.group.GroupDocument
import com.weljak.splitter.utils.mapper.config.GlobalMapperConfig
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

@Component
@Mapper(componentModel = "spring",injectionStrategy = InjectionStrategy.CONSTRUCTOR , config = GlobalMapperConfig::class)
abstract class GroupMapper {
    @Autowired
    private lateinit var clock: Clock

    abstract fun toGroup(groupDocument: GroupDocument): Group
    abstract fun toGroupDocument(group: Group): GroupDocument
    fun toGroupDocument(createGroupForm: CreateGroupForm, createdBy: String): GroupDocument {
        return GroupDocument(
            generateUuid(),
            createGroupForm.groupName,
            createdBy,
            LocalDateTime.now(clock),
            createGroupForm.members?.toList()
        )
    }

    private fun generateUuid(): String {
        return UUID.randomUUID().toString()
    }
}