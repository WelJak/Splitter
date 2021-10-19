package com.weljak.splitter.domain.repository.group

import com.weljak.splitter.domain.model.group.Group
import com.weljak.splitter.domain.model.group.GroupDocument
import com.weljak.splitter.utils.mapper.GroupMapper
import com.weljak.splitter.webapi.controller.group.CreateGroupForm
import mu.KotlinLogging
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class MongoGroupRepository(
    private val mongoGroupDocumentRepository: MongoGroupDocumentRepository,
    private val groupMapper: GroupMapper,
    private val mongoTemplate: ReactiveMongoTemplate
) : GroupRepository {
    private val log = KotlinLogging.logger { }
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

    override fun addMembers(groupDocument: GroupDocument, toAdd: List<String>): Mono<Group> {
        log.info("Adding new members")
        val update = Update()
        val updatedGroupMembers = groupDocument.members!!.plus(toAdd)
        update.set("members", updatedGroupMembers)
        return updateGroupDocumentMembers(groupDocument, update)
            .flatMap { mongoGroupDocumentRepository.findById(groupDocument.id) }
            .map { groupMapper.toGroup(it) }
    }

    override fun removeMembers(groupDocument: GroupDocument, toDelete: List<String>): Mono<Group> {
        log.info("Removing members")
        val update = Update()
        val updatedGroupMembers = groupDocument.members?.minus(toDelete.toSet())
        update.set("members", updatedGroupMembers)
        return updateGroupDocumentMembers(groupDocument, update)
            .flatMap { mongoGroupDocumentRepository.findById(groupDocument.id) }
            .map { groupMapper.toGroup(it) }

    }

    override fun getCurrentUserGroups(currentUser: String): Mono<List<Group>> {
        return mongoTemplate.find(
            query(where("members").`is`(currentUser)),
            GroupDocument::class.java,
            GROUP_COLLECTION_NAME
        )
            .map { groupMapper.toGroup(it) }
            .collectList()
    }

    private fun updateGroupDocumentMembers(groupDocument: GroupDocument, update: Update): Mono<GroupDocument> {
        return mongoTemplate.updateFirst(
            query(where("id").`is`(groupDocument.id)),
            update,
            GroupDocument::class.java,
            GROUP_COLLECTION_NAME
        )
            .flatMap { mongoGroupDocumentRepository.findById(groupDocument.id) }
    }

    companion object {
        private const val GROUP_COLLECTION_NAME = "group"
    }
}