package com.hackaton.accountservice.service.serviceImpl;

import com.hackaton.accountservice.dto.request.DeleteStorageRequest;
import com.hackaton.accountservice.dto.response.DeleteResponse;
import com.hackaton.accountservice.dto.response.StorageServiceResponse;
import com.hackaton.accountservice.exception.NoEntityFoundException;
import com.hackaton.accountservice.feignClient.StorageServiceClient;
import com.hackaton.accountservice.model.AccountModel;
import com.hackaton.accountservice.repository.AccountRepository;
import com.hackaton.accountservice.service.AccountService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final StorageServiceClient storageServiceClient;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, StorageServiceClient storageServiceClient) {
        this.accountRepository = accountRepository;
        this.storageServiceClient = storageServiceClient;
    }

    @Override
    public AccountModel add(AccountModel entity) {
        return accountRepository.save(entity);
    }

    @Override
    public AccountModel getById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new NoEntityFoundException("Account with id: " + id + " doesn't exist"));
    }

    @Override
    public List<AccountModel> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new NoEntityFoundException("Account with id: " + id + " doesn't exist");
        }
        accountRepository.deleteById(id);
    }

    @Override
    public AccountModel update(AccountModel entity) {
        if (!accountRepository.existsById(entity.getId())) {
            throw new NoEntityFoundException("Account with id: " + entity.getId() + " doesn't exist");
        }
        return accountRepository.save(entity);
    }

    public ResponseEntity<Object> uploadAvatar(Long accountId, MultipartFile file, String storageDirectory) {
        StorageServiceResponse storageServiceResponse = storageServiceClient.uploadFile(file, storageDirectory);
        AccountModel account = accountRepository.findById(accountId).orElseThrow(
                () -> new NoEntityFoundException("Account with id: " + accountId + " doesn't exist"));
        account.setAvatarImageUri(storageServiceResponse.getFile_uri());
        accountRepository.save(account);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> deleteAvatar(Long accountId, String storageDirectory) {
        AccountModel account = accountRepository.findById(accountId).orElseThrow(
                () -> new NoEntityFoundException("Account with id: " + accountId + " doesn't exist"));
        account.setAvatarImageUri("DEFAULT");
        try {
            storageServiceClient.deleteFile(new DeleteStorageRequest(account.getAvatarImageUri(), storageDirectory));
            accountRepository.save(account);
            return ResponseEntity.ok().build();
        } catch (FeignException.InternalServerError e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DeleteResponse("The image does not exist or the data was transferred incorrectly"));
        }
    }
}
