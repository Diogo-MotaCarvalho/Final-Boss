package com.finalboss.useCases;

import com.finalboss.domain.YellowEvent;

public interface Publisher {
    YellowEvent publish(YellowEvent message);
}
