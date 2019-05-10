package com.endlesscreator.tibaselib.frame;

import java.util.ArrayList;
import java.util.List;

public class ObservableDefault {

    private List<IObserver> mObservers;

    public ObservableDefault() {
        mObservers = new ArrayList<>();
    }

    public void addObserver(IObserver aObserver) {
        if (aObserver != null && !mObservers.contains(aObserver)) {
            mObservers.add(aObserver);
        }
    }

    public void removeObserver(IObserver aObserver) {
        if (aObserver != null && mObservers.size() > 0) {
            mObservers.remove(aObserver);
        }
    }

    public void updateObserver(Object aObject) {
        for (IObserver lObserver : mObservers) {
            if (lObserver != null) {
                lObserver.update(aObject);
            }
        }
    }
}
