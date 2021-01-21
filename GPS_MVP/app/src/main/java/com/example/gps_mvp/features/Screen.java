package com.example.gps_mvp.features;

import java.util.UUID;

public interface Screen {

    void cachePresenter(Presenter presenter);

    void restorePresenter(UUID uuid);
}
