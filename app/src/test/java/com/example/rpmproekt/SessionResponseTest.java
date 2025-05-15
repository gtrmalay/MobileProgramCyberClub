package com.example.rpmproekt;

import com.example.tdsclub.models.SessionResponse;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SessionResponseTest {

    private SessionResponse sessionResponse;
    private SimpleDateFormat sdf;

    @Before
    public void setUp() throws Exception {
        sessionResponse = new SessionResponse();
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Field sessionTimeField = SessionResponse.class.getDeclaredField("sessionTime");
        sessionTimeField.setAccessible(true);
        sessionTimeField.set(sessionResponse, "2025-05-14T18:00:00Z");

        Field registrationDateField = SessionResponse.class.getDeclaredField("registrationDate");
        registrationDateField.setAccessible(true);
        registrationDateField.set(sessionResponse, "2025-05-14T17:00:00Z");
    }

    @Test
    public void isActive_WhenCurrentTimeBeforeSession_ReturnsTrue() throws Exception {

        Date currentDate = sdf.parse("2025-05-14T17:00:00Z"); // До начала сессии (18:00)

        boolean isActive = sessionResponse.isActive(currentDate);

        if (!isActive) {
            throw new AssertionError("Session should be active when start time is in the future");
        }
    }

    @Test
    public void isActive_WhenCurrentTimeDuringSession_ReturnsTrue() throws Exception {

        Date currentDate = sdf.parse("2025-05-14T18:30:00Z"); // Во время сессии (18:00 - 19:00)

        boolean isActive = sessionResponse.isActive(currentDate);

        if (!isActive) {
            throw new AssertionError("Session should be active during the session");
        }
    }

    @Test
    public void isActive_WhenCurrentTimeAfterSession_ReturnsFalse() throws Exception {

        Date currentDate = sdf.parse("2025-05-14T19:30:00Z"); // После окончания сессии

        boolean isActive = sessionResponse.isActive(currentDate);

        if (isActive) {
            throw new AssertionError("Session should not be active after end time");
        }
    }

    @Test
    public void isActive_WhenCurrentTimeAtSessionStart_ReturnsFalse() throws Exception {

        Date currentDate = sdf.parse("2025-05-14T18:00:00Z"); // Начало сессии

        boolean isActive = sessionResponse.isActive(currentDate);

        if (isActive) {
            throw new AssertionError("Session should not be active exactly at start time");
        }
    }

    @Test
    public void isActive_WhenCurrentTimeAtSessionEnd_ReturnsFalse() throws Exception {

        Date currentDate = sdf.parse("2025-05-14T19:00:00Z"); // Конец сессии

        boolean isActive = sessionResponse.isActive(currentDate);


        if (isActive) {
            throw new AssertionError("Session should not be active exactly at end time");
        }
    }

    @Test
    public void isActive_WhenCurrentTimeBeforeRegistration_ReturnsTrue() throws Exception {

        Date currentDate = sdf.parse("2025-05-14T16:00:00Z"); // До даты регистрации (17:00), но до sessionTime (18:00)

        boolean isActive = sessionResponse.isActive(currentDate);


        if (!isActive) {
            throw new AssertionError("Session should be active when start time is in the future, even before registration date");
        }
    }
}