package com.gymcrm;

import com.gymcrm.application.facade.GymFacade;
import com.gymcrm.infrastructure.config.GymAppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Alish
 */
public class GymApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GymAppConfig.class);
        GymFacade gymFacade = context.getBean(GymFacade.class);
        context.close();
    }
}
