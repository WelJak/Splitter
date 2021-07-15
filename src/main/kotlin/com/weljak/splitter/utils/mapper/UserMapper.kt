package com.weljak.splitter.utils.mapper

import com.weljak.splitter.domain.model.user.Role
import com.weljak.splitter.domain.model.user.User
import com.weljak.splitter.domain.model.user.UserDocument
import com.weljak.splitter.security.CurrentUser
import com.weljak.splitter.security.Pbkdf2Encoder
import com.weljak.splitter.utils.mapper.config.GlobalMapperConfig
import com.weljak.splitter.webapi.controller.user.UserCreationForm
import com.weljak.splitter.webapi.controller.user.UserDto
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, config = GlobalMapperConfig::class)
abstract class UserMapper {
    @Autowired
    private lateinit var pbkdf2Encoder: Pbkdf2Encoder
    abstract fun toUser(userDocument: UserDocument): User
    abstract fun toUserDocument(user: User): UserDocument
    abstract fun toUserDto(user: User): UserDto

    fun toUser(userCreationForm: UserCreationForm): User {
        userCreationForm.let {
            return User(it.username, userCreationForm.email, pbkdf2Encoder.encode(it.password), Role.ROLE_USER, true)
        }
    }

    fun toCurrentUser(user: User): CurrentUser {
        return CurrentUser(user)
    }
}