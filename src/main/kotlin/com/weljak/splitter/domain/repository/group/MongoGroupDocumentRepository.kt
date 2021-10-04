package com.weljak.splitter.domain.repository.group

import com.weljak.splitter.domain.model.group.GroupDocument
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface MongoGroupDocumentRepository: ReactiveMongoRepository<GroupDocument, String> {
    fun findAllBy(groupName: String): Flux<GroupDocument>
}