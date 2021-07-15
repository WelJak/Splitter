package com.weljak.splitter.security

import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.lang.Exception
import java.util.*

@Component
class Pbkdf2Encoder : PasswordEncoder {
    @Value("\${springbootwebfluxjjwt.password.encoder.secret}")
    private lateinit var secret: String

    @Value("\${springbootwebfluxjjwt.password.encoder.iteration}")
    private var iteration: Int = 0

    @Value("\${springbootwebfluxjjwt.password.encoder.keylength}")
    private var keyLength: Int = 0

    override fun encode(rawPassword: CharSequence?): String {
        try {
            val result : ByteArray = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                .generateSecret(PBEKeySpec(rawPassword.toString().toCharArray(), secret.toByteArray(), iteration, keyLength))
                .encoded
            return Base64.getEncoder().encodeToString(result)
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        return encode(rawPassword) == encodedPassword
    }
}