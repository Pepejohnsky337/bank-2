package com.bank.antifraud.util.builder;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.entity.SuspiciousAccountTransferEntity;

public class SuspiciousAccountTransferBuilder {
    
    private static final Long DEFAULT_ID = null;
    private static final Long DEFAULT_ACCOUNT_TRANSFER_ID = 11111L;
    private static final Boolean DEFAULT_IS_BLOCKED = false;
    private static final Boolean DEFAULT_IS_SUSPICIOUS = true;
    private static final String DEFAULT_BLOCKED_REASON = null;
    private static final String DEFAULT_SUSPICIOUS_REASON = "Частые переводы круглых сумм";
    
    private Long id;
    private Long accountTransferId;
    private Boolean isBlocked;
    private Boolean isSuspicious;
    private String blockedReason;
    private String suspiciousReason;
    
    public SuspiciousAccountTransferBuilder() {
        reset();
    }
    
    public SuspiciousAccountTransferBuilder reset() {
        this.id = DEFAULT_ID;
        this.accountTransferId = DEFAULT_ACCOUNT_TRANSFER_ID;
        this.isBlocked = DEFAULT_IS_BLOCKED;
        this.isSuspicious = DEFAULT_IS_SUSPICIOUS;
        this.blockedReason = DEFAULT_BLOCKED_REASON;
        this.suspiciousReason = DEFAULT_SUSPICIOUS_REASON;
        return this;
    }
    
    public SuspiciousAccountTransferBuilder id(Long id) {
        this.id = id;
        return this;
    }
    
    public SuspiciousAccountTransferBuilder accountTransferId(Long accountTransferId) {
        this.accountTransferId = accountTransferId;
        return this;
    }
    
    public SuspiciousAccountTransferBuilder blocked(Boolean isBlocked, String blockedReason) {
        this.isBlocked = isBlocked;
        this.blockedReason = blockedReason;
        return this;
    }
    
    public SuspiciousAccountTransferBuilder suspicious(Boolean isSuspicious, String suspiciousReason) {
        this.isSuspicious = isSuspicious;
        this.suspiciousReason = suspiciousReason;
        return this;
    }
    
    public SuspiciousAccountTransferEntity buildEntity() {
        SuspiciousAccountTransferEntity entity = new SuspiciousAccountTransferEntity();
        entity.setId(id);
        entity.setAccountTransferId(accountTransferId);
        entity.setIsBlocked(isBlocked);
        entity.setIsSuspicious(isSuspicious);
        entity.setBlockedReason(blockedReason);
        entity.setSuspiciousReason(suspiciousReason);
        return entity;
    }
    
    public SuspiciousAccountTransferDto buildDto() {
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        dto.setId(id);
        dto.setAccountTransferId(accountTransferId);
        dto.setIsBlocked(isBlocked);
        dto.setIsSuspicious(isSuspicious);
        dto.setBlockedReason(blockedReason);
        dto.setSuspiciousReason(suspiciousReason);
        return dto;
    }
}
