package ch.hsr_heroes.gadgedothek;

import android.app.Application;

import ch.hsr_heroes.gadgedothek.service.LibraryService;

public class GadgedothekApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LibraryService.setServerAddress("http://localhost:8080/public");
    }
}
