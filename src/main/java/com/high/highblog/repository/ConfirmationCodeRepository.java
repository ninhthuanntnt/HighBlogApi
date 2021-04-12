package com.high.highblog.repository;

import com.high.highblog.enums.CodeType;
import com.high.highblog.model.entity.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationCodeRepository
        extends JpaRepository<ConfirmationCode, Long> {

    Optional<ConfirmationCode> findByIdAndCodeAndCodeType(Long id, String code, CodeType codeType);

    Optional<ConfirmationCode> findByIdAndCodeType(Long id, CodeType codeType);

    List<ConfirmationCode> findByAccountIdAndCodeType(Long accountId, CodeType codeType);
}
