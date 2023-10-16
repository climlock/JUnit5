package com.mishanya.junit;

import com.mishanya.junit.service.UserServiceTest;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.PrintWriter;

public class TestLauncher {

    public static void main(String[] args) {

        var launcher = LauncherFactory.create();
//        launcher.registerLauncherDiscoveryListeners();

        // Собирает статистику после выполнения тестов
        var summaryGeneratingListener = new SummaryGeneratingListener();
//        launcher.registerTestExecutionListeners();
//        launcher.registerTestExecutionListeners(summaryGeneratingListener);
        // Запрос на тест
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
//                .selectors(DiscoverySelectors.selectClass(UserServiceTest.class))
                .selectors(DiscoverySelectors.selectPackage("com.mishanya.junit"))
                .build();

        // Запуск Тестов
        launcher.execute(request, summaryGeneratingListener);

        try (var printWriter = new PrintWriter(System.out)) {
            summaryGeneratingListener.getSummary().printTo(printWriter);
        }
    }
}
