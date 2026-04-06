package com.bank.antifraud.util.builder;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entity.SuspiciousPhoneTransferEntity;

public class SuspiciousPhoneTransferBuilder {
    private static final Long DEFAULT_ID = null;
    private static final Long DEFAULT_PHONE_TRANSFER_ID = 11111L;
    private static final Boolean DEFAULT_IS_BLOCKED = false;
    private static final Boolean DEFAULT_IS_SUSPICIOUS = true;
    private static final String DEFAULT_BLOCKED_REASON = null;
    private static final String DEFAULT_SUSPICIOUS_REASON = "Частые переводы круглых сумм";
    
    private Long id;
    private Long phoneTransferId;
    private Boolean isBlocked;
    private Boolean isSuspicious;
    private String blockedReason;
    private String suspiciousReason;
    
    public SuspiciousPhoneTransferBuilder() {
        reset();
    }
    
    public SuspiciousPhoneTransferBuilder reset() {
        this.id = DEFAULT_ID;
        this.phoneTransferId = DEFAULT_PHONE_TRANSFER_ID;
        this.isBlocked = DEFAULT_IS_BLOCKED;
        this.isSuspicious = DEFAULT_IS_SUSPICIOUS;
        this.blockedReason = DEFAULT_BLOCKED_REASON;
        this.suspiciousReason = DEFAULT_SUSPICIOUS_REASON;
        return this;
    }
    
    public SuspiciousPhoneTransferBuilder id(Long id) {
        this.id = id;
        return this;
    }
    
    public SuspiciousPhoneTransferBuilder phoneTransferId(Long phoneTransferId) {
        this.phoneTransferId = phoneTransferId;
        return this;
    }
    
    public SuspiciousPhoneTransferBuilder blocked(Boolean isBlocked, String blockedReason) {
        this.isBlocked = isBlocked;
        this.blockedReason = blockedReason;
        return this;
    }
    
    public SuspiciousPhoneTransferBuilder suspicious(Boolean isSuspicious, String suspiciousReason) {
        this.isSuspicious = isSuspicious;
        this.suspiciousReason = suspiciousReason;
        return this;
    }
    
    public SuspiciousPhoneTransferEntity buildEntity() {
        SuspiciousPhoneTransferEntity entity = new SuspiciousPhoneTransferEntity();
        entity.setId(id);
        entity.setPhoneTransferId(phoneTransferId);
        entity.setIsBlocked(isBlocked);
        entity.setIsSuspicious(isSuspicious);
        entity.setBlockedReason(blockedReason);
        entity.setSuspiciousReason(suspiciousReason);
        return entity;
    }
    
    public SuspiciousPhoneTransferDto buildDto() {
        SuspiciousPhoneTransferDto dto = new SuspiciousPhoneTransferDto();
        dto.setId(id);
        dto.setPhoneTransferId(phoneTransferId);
        dto.setIsBlocked(isBlocked);
        dto.setIsSuspicious(isSuspicious);
        dto.setBlockedReason(blockedReason);
        dto.setSuspiciousReason(suspiciousReason);
        return dto;
    }
}
