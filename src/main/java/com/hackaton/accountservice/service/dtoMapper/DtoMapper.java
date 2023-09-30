package com.hackaton.accountservice.service.dtoMapper;

public interface DtoMapper<M, R, S> {
    M toModel(R dto);

    S toDto(M model);
}
