package de.jadenk.springcloud.service;

import de.jadenk.springcloud.exception.CustomRuntimeException;
import de.jadenk.springcloud.model.Log;
import de.jadenk.springcloud.model.User;
import de.jadenk.springcloud.repository.LogRepository;
import de.jadenk.springcloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CloudSettingService cloudSettingService;

    @Autowired
    private LogRepository logRepo;

    public Log log(String username, String action) {

        if (!cloudSettingService.getBooleanSetting("ENABLE_LOGGING", true)) {
            return null;
        }

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new CustomRuntimeException("[Log Service] User not found with name " + username));

        Log log = new Log();
        log.setUser(user);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());

        logRepo.save(log);
        return log;
    }

    @Autowired
    private LogRepository logRepository;

    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }
}
