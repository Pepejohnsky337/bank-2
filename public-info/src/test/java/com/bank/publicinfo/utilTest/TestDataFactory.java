package com.bank.publicinfo.utilTest;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.entity.AtmEntity;
import com.bank.publicinfo.entity.AuditEntity;
import com.bank.publicinfo.entity.BankDetailsEntity;
import com.bank.publicinfo.entity.BranchEntity;
import com.bank.publicinfo.entity.CertificateEntity;
import com.bank.publicinfo.entity.LicenseEntity;

import java.math.BigDecimal;
import java.time.LocalTime;

@SuppressWarnings("checkstyle:MultipleStringLiterals")
public final class TestDataFactory {

    public static AtmDto createAtmDto(Long id, String address) {
        return AtmDto.builder()
                .id(id)
                .address(address)
                .startOfWork(LocalTime.of(9, 0))
                .endOfWork(LocalTime.of(23, 0))
                .allHours(true)
                .build();
    }

    public static AtmEntity createAtmEntity(Long id, String address) {
        return AtmEntity.builder()
                .id(id)
                .address(address)
                .startOfWork(LocalTime.of(8, 0))
                .endOfWork(LocalTime.of(22, 0))
                .allHours(false)
                .build();
    }

    public static LicenseEntity createLicenseEntity(Long id) {
        return LicenseEntity.builder()
                .id(id)
                .photoLicense(new Byte[]{1, 2, 3, 4, 5})
                .build();
    }

    public static LicenseDto createLicenseDto(Long id) {
        return LicenseDto.builder()
                .id(id)
                .photoLicense(new Byte[]{1, 2, 3, 4, 5})
                .build();
    }

    public static CertificateEntity createCertificateEntity(Long id) {
        return CertificateEntity.builder()
                .id(id)
                .photoCertificate(new Byte[]{1, 2, 3, 4, 5})
                .build();
    }

    public static CertificateDto createCertificateDto(Long id) {
        return CertificateDto.builder()
                .id(id)
                .photoCertificate(new Byte[]{1, 2, 3, 4, 5})
                .build();
    }

    public static BranchEntity createBranchEntity(Long id, String city) {
        return BranchEntity.builder()
                .id(id)
                .address("Москва")
                .phoneNumber(123456789L)
                .city(city)
                .startOfWork(LocalTime.of(8, 0))
                .endOfWork(LocalTime.of(22, 0))
                .build();
    }

    public static BranchDto createBranchDto(Long id, String city) {
        return BranchDto.builder()
                .id(id)
                .address("Москва")
                .phoneNumber(123456789L)
                .city(city)
                .startOfWork(LocalTime.of(8, 0))
                .endOfWork(LocalTime.of(22, 0))
                .build();
    }

    public static BranchDto createBranchDtoNotAllFields(Long id, String city) {
        return BranchDto.builder()
                .id(id)
                .address("Москва")
                .phoneNumber(123456789L)
                .city(city)
                .build();
    }

    public static BankDetailsDto createBankDetailsDto(Long id, String name) {
        return BankDetailsDto.builder()
                .id(id)
                .bik(123456789L)
                .inn(123L)
                .kpp(456L)
                .corAccount(BigDecimal.valueOf(789))
                .city("Москва")
                .jointStockCompany("jointStockCompany")
                .name(name)
                .build();
    }

    public static BankDetailsEntity createBankDetailsEntity(Long id, String name) {
        return BankDetailsEntity.builder()
                .id(id)
                .bik(123456789L)
                .inn(123L)
                .kpp(456L)
                .corAccount(BigDecimal.valueOf(789))
                .city("Москва")
                .jointStockCompany("jointStockCompany")
                .name(name)
                .build();
    }

    public static BankDetailsDto createBankDetailsDtoNotAllFields(Long id, String name) {
        return BankDetailsDto.builder()
                .id(id)
                .city("Москва")
                .jointStockCompany("jointStockCompany")
                .name(name)
                .build();
    }

    public static AuditDto createAuditDto(Long id, String entityType) {
        return AuditDto.builder()
                .id(id)
                .entityType(entityType)
                .operationType("CREATE")
                .createdBy("admin")
                .modifiedBy("admin")
                .newEntityJson("new entity json")
                .entityJson("old entity json")
                .build();
    }

    public static AuditEntity createAuditEntity(Long id, String entityType) {
        return AuditEntity.builder()
                .id(id)
                .entityType(entityType)
                .build();
    }
}
