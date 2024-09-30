package com.finalboss.useCases;

import com.finalboss.domain.MarketUpdate;
import com.finalboss.domain.YellowEvent;

public interface Publisher {
    void publish(YellowEvent message);
}
