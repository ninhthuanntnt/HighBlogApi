package com.high.highblog.repository;

import com.high.highblog.enums.CodeType;
import com.high.highblog.model.entity.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationCodeRepository
        extends JpaRepository<ConfirmationCode, Long> {

    Optional<ConfirmationCode> findByIdAndCodeAndCodeType(Long id, String code, CodeType codeType);
}
