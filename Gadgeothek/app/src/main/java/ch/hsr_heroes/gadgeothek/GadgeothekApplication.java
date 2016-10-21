package ch.hsr_heroes.gadgeothek;

import android.app.Application;

import ch.hsr_heroes.gadgeothek.service.LibraryService;

public class GadgeothekApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LibraryService.setServerAddress("http://localhost:8080/public");
    }
}
