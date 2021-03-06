package com.high.highblog.bloc;

import com.high.highblog.enums.CodeType;
import com.high.highblog.enums.RoleType;
import com.high.highblog.error.exception.ObjectNotFoundException;
import com.high.highblog.error.exception.ValidatorException;
import com.high.highblog.model.dto.request.RegisterReq;
import com.high.highblog.model.entity.Account;
import com.high.highblog.model.entity.ConfirmationCode;
import com.high.highblog.model.entity.Role;
import com.high.highblog.model.entity.User;
import com.high.highblog.helper.SecurityHelper;
import com.high.highblog.service.AccountRoleService;
import com.high.highblog.service.AccountService;
import com.high.highblog.service.ConfirmationCodeService;
import com.high.highblog.service.RoleService;
import com.high.highblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class RegisterBloc {
    private final UserService userService;
    private final AccountService accountService;
    private final AccountRoleService accountRoleService;
    private final RoleService roleService;
    private final ConfirmationCodeService confirmationCodeService;

    private final MailBloc mailBloc;
    private final static RoleType DEFAULT_ACCOUNT_ROLE = RoleType.ROLE_INACTIVE_USER;

    public RegisterBloc(final UserService userService,
                        final AccountService accountService,
                        final AccountRoleService accountRoleService,
                        final RoleService roleService,
                        final ConfirmationCodeService confirmationCodeService,
                        final MailBloc mailBloc) {
        this.userService = userService;
        this.accountService = accountService;
        this.accountRoleService = accountRoleService;
        this.roleService = roleService;
        this.confirmationCodeService = confirmationCodeService;

        this.mailBloc = mailBloc;
    }

    @Transactional
    public void register(final RegisterReq registerReq) {

        validateRegisterReq(registerReq);

        User user = User.builder()
                        .firstName(registerReq.getFirstName()).lastName(registerReq.getLastName())
                        .genderType(registerReq.getGenderType())
                        .build();
        userService.save(user);

        Account account = Account.builder()
                                 .userId(user.getId())
                                 .username(registerReq.getUsername())
                                 .password(registerReq.getPassword())
                                 .email(registerReq.getEmail())
                                 .build();
        accountService.save(account);

        Role role = roleService.getRoleByRoleType(DEFAULT_ACCOUNT_ROLE);
        accountRoleService.save(account.getId(), Collections.singletonList(role.getId()));

        ConfirmationCode confirmationCode = confirmationCodeService.save(account.getId(),
                                                                         generateUUID(),
                                                                         CodeType.REGISTRATION);

        mailBloc.sendConfirmRegistrationMailTo("ninhthuan1999@gmail.com",
                                               registerReq.getReturnUrl(),
                                               confirmationCode);
    }

    @Transactional
    public void activateAccount(final Long confirmationCodeId, final String code) {
        log.info("Activate account with confirmationCodeId #{} and code #{}", confirmationCodeId, code);

        ConfirmationCode confirmationCode = confirmationCodeService.getByIdAndCodeAndType(confirmationCodeId,
                                                                                          code,
                                                                                          CodeType.REGISTRATION);
        validateConfirmationCode(confirmationCode);

        this.activate(confirmationCode.getAccountId());
    }

    private void validateConfirmationCode(final ConfirmationCode confirmationCode) {
        Long accountId = confirmationCode.getAccountId();

        if (ObjectUtils.isEmpty(accountId)) {
            throw new ObjectNotFoundException("accountId");
        } else if (Objects.nonNull(SecurityHelper.getAccountId())
                && ObjectUtils.notEqual(SecurityHelper.getAccountId(), accountId)) {
            // For use-case user login into system then activate account
            throw new ValidatorException("Mismatch account id", "accountId");
        } else if (confirmationCode.isExpired()){
            throw new ValidatorException("confirmationCode", "confirmationCode");
        }
    }

    private void validateRegisterReq(final RegisterReq registerReq) {
        if (accountService.isExistAccountByUsername(registerReq.getUsername()))
            throw new ValidatorException("Duplicate username", "username");
        else if (accountService.isExistAccountByEmail(registerReq.getEmail()))
            throw new ValidatorException("Duplicate email", "email");

    }

    private void activate(final Long accountId) {
        Role role = roleService.getRoleByRoleType(RoleType.ROLE_USER);
        accountRoleService.deleteOldAndSave(accountId, Collections.singletonList(role.getId()));
    }

    private String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
