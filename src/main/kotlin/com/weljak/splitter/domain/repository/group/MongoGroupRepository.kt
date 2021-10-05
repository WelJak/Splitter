package com.weljak.splitter.domain.repository.group

import com.weljak.splitter.webapi.controller.group.CreateGroupForm
import com.weljak.splitter.domain.model.group.Group
import com.weljak.splitter.domain.model.group.GroupDocument
import com.weljak.splitter.utils.mapper.GroupMapper
import mu.KotlinLogging
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Repository
class MongoGroupRepository(
    private val mongoGroupDocumentRepository: MongoGroupDocumentRepository,
    private val groupMapper: GroupMapper,
    private val mongoTemplate: ReactiveMongoTemplate
    ): GroupRepository {
    val log = KotlinLogging.logger {  }
    override fun createGroup(createGroupForm: CreateGroupForm, createdBy: String): Mono<Group> {
        val groupDocument = groupMapper.toGroupDocument(createGroupForm, createdBy)
        return mongoGroupDocumentRepository.save(groupDocument).map { groupMapper.toGroup(it) }
    }

    override fun deleteById(id: String): Mono<Void> {
        return mongoGroupDocumentRepository.deleteById(id)
    }

    override fun findById(id: String): Mono<Group> {
        return mongoGroupDocumentRepository.findById(id).map { groupMapper.toGroup(it) }
    }

    override fun findByGroupName(groupName: String): Mono<List<Group>> {
        return mongoGroupDocumentRepository.findAllBy(groupName)
            .map { groupMapper.toGroup(it) }
            .collectList()

    }

    override fun addMembers(groupId: String, toAdd: List<String>): Mono<Group> {
        return mongoGroupDocumentRepository.findById(groupId)
            .map { it.copy(members = it.members?.plus(toAdd)) }
            .map { updateGroupDocumentMembers(it) }
            .flatMap { it }
            .map { groupMapper.toGroup(it) }
    }

    override fun removeMembers(groupId: String, toDelete: List<String>): Mono<Group> {
        return mongoGroupDocumentRepository.findById(groupId)
                .map { it.copy(members = it.members?.minus(toDelete)) }
                .flatMap { updateGroupDocumentMembers(it) }
                .map { groupMapper.toGroup(it) }
    }

    override fun getCurrentUserGroups(currentUser: String): Mono<List<Group>> {
        return mongoTemplate.find(query(where("members").and(currentUser)), GroupDocument::class.java, "group")
            .map { groupMapper.toGroup(it) }
            .collectList()
    }

    private fun updateGroupDocumentMembers(groupDocument: GroupDocument): Mono<GroupDocument> {
        log.info("update")
        val update = Update()
        update.set("members", groupDocument.members)
        return mongoTemplate.updateFirst(query(where("id").`is`(groupDocument.id)), update, GroupDocument::class.java, "group")
            .flatMap { mongoGroupDocumentRepository.findById(groupDocument.id) }
    }
}