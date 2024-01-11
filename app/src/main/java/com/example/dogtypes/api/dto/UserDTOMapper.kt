package com.example.dogtypes.api.dto

import com.example.dogtypes.domain.User

class UserDTOMapper {
    fun fromUserDTOToUserDomain(userDTO: UserDTO) =
       User(userDTO.id, userDTO.email, userDTO.authenticationToken)
}