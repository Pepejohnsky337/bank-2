package com.bank.account.service.feign.service;

import com.bank.account.dto.feign.ProfileDto;
import com.bank.account.feign.client.ProfileClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "profiles")
public class ProfileServiceImpl implements ProfileService {

    private final ProfileClient profileClient;

    @SneakyThrows
    @Override
    @Cacheable(key = "#id")
    public ProfileDto getProfileById(Long id) {
        log.info("Обращаемся к PROFILE-APP за профилем с id: {}", id);
        Thread.sleep(3000);
        final ProfileDto profileDto = profileClient.getProfileById(id);
        log.info("Профиль с id {} успешно получен и будет закеширован", id);
        return profileDto;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void evict() {
        log.info("Cache 'profiles' cleared");
    }
}
