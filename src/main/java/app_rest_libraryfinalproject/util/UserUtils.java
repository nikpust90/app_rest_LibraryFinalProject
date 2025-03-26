package app_rest_libraryfinalproject.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;

public class UserUtils {

    // Получить имя текущего пользователя
    public static String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return principal.toString();
    }

    // Получить текущее время
    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}