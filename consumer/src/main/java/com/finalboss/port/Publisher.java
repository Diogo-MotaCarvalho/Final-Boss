package com.finalboss.port;

import com.finalboss.domain.YellowEvent;

public interface Publisher {
    void publish(YellowEvent message);
}
