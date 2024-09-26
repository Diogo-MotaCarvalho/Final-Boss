package com.finalboss.useCases;

import com.finalboss.domain.YellowEvent;

public interface Consumer {
    YellowEvent buildYellowEvent();
    void readData();
}
