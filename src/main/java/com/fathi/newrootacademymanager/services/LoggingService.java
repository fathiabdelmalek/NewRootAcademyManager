package com.fathi.newrootacademymanager.services;

import com.fathi.newrootacademymanager.helpers.enums.ActivityType;
import com.fathi.newrootacademymanager.models.Activity;

import java.util.logging.Logger;

public class LoggingService {
    private static final Logger logger = Logger.getLogger(LoggingService.class.getName());

    private LoggingService() {}

    public static void add(String msg) {
        log(msg, ActivityType.Add);
    }

    public static void update(String msg) {
        log(msg, ActivityType.Update);
    }

    public static void delete(String msg) {
        log(msg, ActivityType.Delete);
    }

    public static void enroll(String msg) {
        log(msg, ActivityType.Enroll);
    }

    public static void leave(String msg) {
        log(msg, ActivityType.Leave);
    }

    public static void pay(String msg) {
        log(msg, ActivityType.Pay);
    }

    public static void receive(String msg) {
        log(msg, ActivityType.Receive);
    }

    public static void error(String msg) {
        logger.severe(msg);
        Activity activity = new Activity(ActivityType.Error, msg);
        CRUDService.create(activity);
    }

    private static void log(String msg, ActivityType type) {
        logger.info(msg);
        Activity activity = new Activity(type, msg);
        CRUDService.create(activity);
    }
}
