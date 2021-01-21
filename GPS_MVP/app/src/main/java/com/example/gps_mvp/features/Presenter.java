package com.example.gps_mvp.features;

import java.util.UUID;

public interface Presenter {

    /*
     The method is used to restore the view
     reference in the presenter, after an
     orientation change
     */
    void setScreen(Screen screen);

    /*
     The UUID is used to save and restore
     the presenter instance during an
     orientation change
     */
    UUID getUuid();
}
