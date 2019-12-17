package com.armpits.nice.notifications;

import java.util.concurrent.atomic.AtomicInteger;

class NotificationID {
        // AtomicInteger allows for concurrent incrementation in a thread-safe manor
        // without synchronizing the access to the variable.
        private final static AtomicInteger c = new AtomicInteger(0);
        // Atomic operation can safely be called from multiple threads.
        static int getID() {
            return c.incrementAndGet();
        }
}
