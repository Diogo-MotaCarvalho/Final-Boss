package com.finalboss.useCases;

import com.finalboss.domain.MarketUpdate;

public interface Publisher {
    void publish(MarketUpdate message);
}
