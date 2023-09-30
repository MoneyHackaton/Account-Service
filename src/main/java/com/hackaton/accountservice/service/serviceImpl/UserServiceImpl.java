package com.hackaton.accountservice.service.serviceImpl;

import com.hackaton.accountservice.exception.NoEntityFoundException;
import com.hackaton.accountservice.model.ProfileModel;
import com.hackaton.accountservice.repository.UserRepository;
import com.hackaton.accountservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ProfileModel add(ProfileModel entity) {
        return userRepository.save(entity);
    }

    @Override
    public ProfileModel getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoEntityFoundException("User with id: " + id + " doesn't exist"));
    }

    @Override
    public List<ProfileModel> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoEntityFoundException("User with id: " + id + " doesn't exist");
        }
        userRepository.deleteById(id);
    }

    @Override
    public ProfileModel update(ProfileModel entity) {
        if (userRepository.existsById(entity.getId())) {
            throw new NoEntityFoundException("User with id: " + entity.getId() + " doesn't exist");
        }
        return userRepository.save(entity);
    }
}
