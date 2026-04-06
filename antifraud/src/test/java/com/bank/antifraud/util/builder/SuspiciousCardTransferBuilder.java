package com.bank.antifraud.util.builder;

import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.entity.SuspiciousCardTransferEntity;

public class SuspiciousCardTransferBuilder {
    private static final Long DEFAULT_ID = null;
    private static final Long DEFAULT_CARD_TRANSFER_ID = 11111L;
    private static final Boolean DEFAULT_IS_BLOCKED = false;
    private static final Boolean DEFAULT_IS_SUSPICIOUS = true;
    private static final String DEFAULT_BLOCKED_REASON = null;
    private static final String DEFAULT_SUSPICIOUS_REASON = "Частые переводы круглых сумм";
    
    private Long id;
    private Long cardTransferId;
    private Boolean isBlocked;
    private Boolean isSuspicious;
    private String blockedReason;
    private String suspiciousReason;
    
    public SuspiciousCardTransferBuilder() {
        reset();
    }
    
    public SuspiciousCardTransferBuilder reset() {
        this.id = DEFAULT_ID;
        this.cardTransferId = DEFAULT_CARD_TRANSFER_ID;
        this.isBlocked = DEFAULT_IS_BLOCKED;
        this.isSuspicious = DEFAULT_IS_SUSPICIOUS;
        this.blockedReason = DEFAULT_BLOCKED_REASON;
        this.suspiciousReason = DEFAULT_SUSPICIOUS_REASON;
        return this;
    }
    
    public SuspiciousCardTransferBuilder id(Long id) {
        this.id = id;
        return this;
    }
    
    public SuspiciousCardTransferBuilder cardTransferId(Long cardTransferId) {
        this.cardTransferId = cardTransferId;
        return this;
    }
    
    public SuspiciousCardTransferBuilder blocked(Boolean isBlocked, String blockedReason) {
        this.isBlocked = isBlocked;
        this.blockedReason = blockedReason;
        return this;
    }
    
    public SuspiciousCardTransferBuilder suspicious(Boolean isSuspicious, String suspiciousReason) {
        this.isSuspicious = isSuspicious;
        this.suspiciousReason = suspiciousReason;
        return this;
    }
    
    public SuspiciousCardTransferEntity buildEntity() {
        SuspiciousCardTransferEntity entity = new SuspiciousCardTransferEntity();
        entity.setId(id);
        entity.setCardTransferId(cardTransferId);
        entity.setIsBlocked(isBlocked);
        entity.setIsSuspicious(isSuspicious);
        entity.setBlockedReason(blockedReason);
        entity.setSuspiciousReason(suspiciousReason);
        return entity;
    }
    
    public SuspiciousCardTransferDto buildDto() {
        SuspiciousCardTransferDto dto = new SuspiciousCardTransferDto();
        dto.setId(id);
        dto.setCardTransferId(cardTransferId);
        dto.setIsBlocked(isBlocked);
        dto.setIsSuspicious(isSuspicious);
        dto.setBlockedReason(blockedReason);
        dto.setSuspiciousReason(suspiciousReason);
        return dto;
    }
}
