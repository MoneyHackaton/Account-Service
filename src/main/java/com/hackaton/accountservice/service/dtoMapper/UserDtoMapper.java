package com.hackaton.accountservice.service.dtoMapper;

import com.hackaton.accountservice.dto.request.UserRequest;
import com.hackaton.accountservice.dto.response.UserResponse;
import com.hackaton.accountservice.model.AccountModel;
import com.hackaton.accountservice.model.ProfileModel;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserDtoMapper implements DtoMapper<ProfileModel, UserRequest, UserResponse> {

    @Override
    public ProfileModel toModel(UserRequest dto) {
        AccountModel accountModel = new AccountModel();
        accountModel.setEmail(dto.getEmail());
        accountModel.setRegistryDate(dto.getRegistryDate());
        accountModel.setAvatarImageUri("DEFAULT");
        accountModel.setIncome(dto.getIncome());
        ProfileModel profileModel = new ProfileModel();
        profileModel.setFirstName(dto.getFirstName());
        profileModel.setLastName(dto.getLastName());
        profileModel.setPassportId(dto.getPassportId());
        profileModel.setCountry(dto.getCountry());
        profileModel.setDateOfBirth(dto.getDateOfBirth());
        profileModel.setAccount(accountModel);
        profileModel.setAge(LocalDate.now().getYear() - dto.getDateOfBirth().getYear());
        return profileModel;
    }

    @Override
    public UserResponse toDto(ProfileModel model) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(model.getId());
        userResponse.setFirstName(model.getFirstName());
        userResponse.setLastName(model.getLastName());
        userResponse.setPassportId(model.getPassportId());
        userResponse.setCountry(model.getCountry());
        userResponse.setDateOfBirth(model.getDateOfBirth());
        userResponse.setAge(model.getAge());
        userResponse.setAccount(model.getAccount());
        return userResponse;
    }
}
